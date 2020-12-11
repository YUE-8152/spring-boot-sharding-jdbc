package com.cardlan.sharding.jdbc.common.sharding;

import com.cardlan.sharding.jdbc.common.utils.DateUtils;
import com.google.common.collect.Range;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @ProjectName: cln-school-app
 * @Package: com.cln.bi.common.util
 * @ClassName: ShardingTableUtils
 * @Author: YUE
 * @Description:
 * @Date: 2020/12/3 9:12
 * @Version: 1.0
 */
public class ShardingTableUtils {
    private final static DateTimeFormatter createDateDf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static String END_TIME_2020 = "2020-12-31 23:59:59";
    private final static String START_TIME_2021 = "2021-01-01 00:00:00";

    public static Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue, String columnName) {
        Set<String> finalTargetNames = new HashSet<>();
        if (shardingValue.getColumnNameAndShardingValuesMap().size() > 0) {
            /**
             * 以下是对精确分片的处理，比如= 或者 in
             */
            Map<String, Collection<Comparable<?>>> map = shardingValue.getColumnNameAndShardingValuesMap();
            Map<String, Collection<Comparable<?>>> shardingValuesMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            shardingValuesMap.putAll(map);
            for (String availableTargetName : availableTargetNames) {
                String ym = availableTargetName.substring(availableTargetName.length() - 6);
                if (shardingValuesMap.get(columnName) != null) {
                    if (shardingValuesMap.get(columnName).size() > 0) {
                        Collection<Comparable<?>> createDateStr = shardingValuesMap.get(columnName);
                        for (Comparable<?> date : createDateStr) {
                            String toYM = null;
                            if (date instanceof Timestamp) {
                                // 小于2021年的数据依然存放原始表中
                                String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                                if (DateUtils.compareDate(END_TIME_2020, format)) {
                                    toYM = null;
                                } else {
                                    LocalDate timestamp = ((Timestamp) date).toLocalDateTime().toLocalDate();
                                    Date localDateToDate = DateUtils.localDateToDate(timestamp);
                                    toYM = DateUtils.getDateToStringYM(localDateToDate);
                                }
                            } else if (date instanceof String) {
                                if (DateUtils.compareDate(END_TIME_2020, (String) date)) {
                                    toYM = null;
                                } else {
                                    toYM = DateUtils.getDateToStringYM(DateUtils.getStringTimeToDate((String) date, DateUtils.DEFAULT_DATE_TIME_FORMAT));
                                }
                            } else if (date instanceof Date) {
                                if (DateUtils.compareDate(DateUtils.getStringTimeToDate(END_TIME_2020, DateUtils.DEFAULT_DATE_TIME_FORMAT), (Date) date)) {
                                    toYM = null;
                                } else {
                                    toYM = DateUtils.getDateToStringYM((Date) date);
                                }
                            }
                            if (ym.equals(toYM)) {
                                finalTargetNames.add(availableTargetName);
                            }
                        }
                    }
                }
            }
            if (CollectionUtils.isEmpty(finalTargetNames)) {
                finalTargetNames.add(shardingValue.getLogicTableName());
            }
        } else if (shardingValue.getColumnNameAndRangeValuesMap().size() > 0) {
            /**
             * 以下是对范围分片的处理，比如>、<等
             * 这里主要处理create_date_，通过create_date_来确认是那些表
             */
            Map<String, Range<Comparable<?>>> rangeValuesMap = shardingValue.getColumnNameAndRangeValuesMap();
            Range<Comparable<?>> createDate = rangeValuesMap.get(columnName);
            //2020-05-19 00:00:00
            LocalDate lowerEndpointDt = null, upperEndpointDt = null;
            boolean isContain = false;
            if (createDate.hasLowerBound() && createDate.hasUpperBound()) {
                // 时间段：startTime=<2020-12-31 23:59:59 && endTime>=2020-12-31 23:59:59 && endTime>=当前时间
                if (DateUtils.compareDate(END_TIME_2020, (String) createDate.lowerEndpoint()) &&
                        DateUtils.compareDate((String) createDate.upperEndpoint(), END_TIME_2020) &&
                        DateUtils.compareDate((String) createDate.upperEndpoint(), DateUtils.getCurrentDateTime())) {
                    lowerEndpointDt = LocalDateTime.parse(START_TIME_2021, createDateDf).toLocalDate();
                    upperEndpointDt = LocalDateTime.parse(DateUtils.getCurrentDateTime(), createDateDf).toLocalDate();
                    isContain = true;
                    // 时间段：startTime=<2020-12-31 23:59:59 && endTime>=2020-12-31 23:59:59 && endTime=<当前时间
                } else if (DateUtils.compareDate(END_TIME_2020, (String) createDate.lowerEndpoint()) &&
                        DateUtils.compareDate((String) createDate.upperEndpoint(), END_TIME_2020) &&
                        DateUtils.compareDate(DateUtils.getCurrentDateTime(), (String) createDate.upperEndpoint())) {
                    lowerEndpointDt = LocalDateTime.parse(START_TIME_2021, createDateDf).toLocalDate();
                    upperEndpointDt = LocalDateTime.parse((String) createDate.upperEndpoint(), createDateDf).toLocalDate();
                    isContain = true;
                    //  时间段：startTime>=2020-12-31 23:59:59 && endTime>=当前时间
                } else if (DateUtils.compareDate((String) createDate.lowerEndpoint(), END_TIME_2020) &&
                        DateUtils.compareDate((String) createDate.upperEndpoint(), DateUtils.getCurrentDateTime())) {
                    lowerEndpointDt = LocalDateTime.parse(START_TIME_2021, createDateDf).toLocalDate();
                    upperEndpointDt = LocalDateTime.parse(DateUtils.getCurrentDateTime(), createDateDf).toLocalDate();
                    //  时间段：startTime>=2020-12-31 23:59:59 && endTime>=当前时间
                } else if (DateUtils.compareDate((String) createDate.lowerEndpoint(), END_TIME_2020) &&
                        DateUtils.compareDate(DateUtils.getCurrentDateTime(), (String) createDate.upperEndpoint())) {
                    lowerEndpointDt = LocalDateTime.parse(START_TIME_2021, createDateDf).toLocalDate();
                    upperEndpointDt = LocalDateTime.parse((String) createDate.upperEndpoint(), createDateDf).toLocalDate();
                }
            }
            finalTargetNames = finalTargetNames(availableTargetNames, shardingValue, finalTargetNames,
                    lowerEndpointDt, upperEndpointDt, isContain);
        }
        return finalTargetNames;
    }

    /**
     * 根据查询时间获取表名
     *
     * @param availableTargetNames:
     * @param shardingValue:
     * @param finalTargetNames:
     * @param lowerEndpointDt:
     * @param upperEndpointDt:
     * @param isContain:
     * @return: java.util.Set<java.lang.String>
     * @Author: YUE
     * @Date: 2020/12/3 14:09
     **/
    private static Set<String> finalTargetNames(Collection<String> availableTargetNames, ComplexKeysShardingValue<Comparable<?>> shardingValue,
                                                Set<String> finalTargetNames, LocalDate lowerEndpointDt, LocalDate upperEndpointDt, boolean isContain) {
        if (lowerEndpointDt != null && upperEndpointDt != null) {
            Set<String> lowerSet = new HashSet<>();
            Set<String> upperSet = new HashSet<>();
            for (String availableTargetName : availableTargetNames) {
                String ym = availableTargetName.substring(availableTargetName.length() - 6);
                LocalDate targetNamesDt = getLocalDateFromTableName(ym);
                if (lowerEndpointDt != null && (targetNamesDt.isAfter(lowerEndpointDt) || isSameMonth(lowerEndpointDt, targetNamesDt))) {
                    lowerSet.add(availableTargetName);
                }
                if (upperEndpointDt != null && (targetNamesDt.isBefore(upperEndpointDt) || (isSameMonth(upperEndpointDt, targetNamesDt)))) {
                    upperSet.add(availableTargetName);
                }
            }
            lowerSet.retainAll(upperSet);
            finalTargetNames = lowerSet;
            if (isContain) {
                finalTargetNames.add(shardingValue.getLogicTableName());
            }
        } else {
            finalTargetNames.add(shardingValue.getLogicTableName());
        }
        return finalTargetNames;
    }

    /**
     * 是否是同一个月
     *
     * @param date       待比较日期
     * @param targetDate 目标日期
     * @return 真为是同一个月
     */
    private static boolean isSameMonth(LocalDate date, LocalDate targetDate) {
        return targetDate.getYear() == date.getYear() && targetDate.getMonthValue() == date.getMonthValue();
    }


    private static LocalDate getLocalDateFromTableName(String ym) {
        String y = ym.substring(0, 4);
        String m = ym.substring(ym.length() - 2);
        String dateTime = y + "-" + m + "-01 00:00:00";
        return DateUtils.dateToLocalDate(DateUtils.getStringTimeToDate(dateTime, "yyyy-MM-dd HH:mm:ss"));
    }
}
