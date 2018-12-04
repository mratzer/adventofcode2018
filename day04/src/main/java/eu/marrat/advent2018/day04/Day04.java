/*
 * Copyright 2018 Markus Ratzer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.marrat.advent2018.day04;

import eu.marrat.advent2018.common.ClasspathFileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day04 {

    private static final Pattern PATTERN = Pattern.compile("\\[([\\d -:]+)] (Guard #(\\d+) begins shift|.+)");

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm")
            .toFormatter();

    public static void main(String[] args) {
        Map<Integer, Guard> guards = getGuardsAndTheirEvents();

        Guard longSleeper = guards.values().stream()
                .max(Comparator.comparing(Guard::getSleepTimeMinutes))
                .orElseThrow(IllegalStateException::new);

        System.out.println(guards);
        System.out.println(longSleeper);
        System.out.println(longSleeper.id * longSleeper.getMostSleepyMinute());

        Guard guard = guards.values().stream()
                .max(Comparator.comparing(Guard::countsOfMostSleepyMinute))
                .orElseThrow(IllegalStateException::new);

        System.out.println(guard.id * guard.getMostSleepyMinute());
    }

    private static Map<Integer, Guard> getGuardsAndTheirEvents() {
        Map<Integer, Guard> guards = new HashMap<>();

        Guard currentGuard = null;

        List<String> events = ClasspathFileUtils.getLines("input")
                .sorted()
                .collect(Collectors.toList());

        for (String event : events) {
            Matcher matcher = PATTERN.matcher(event);
            if (matcher.matches()) {
                LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1), FORMATTER);
                GuardEventType guardEventType = GuardEventType.fromString(matcher.group(2));
                Integer guardId = matcher.group(3) == null ? null : Integer.valueOf(matcher.group(3));

                if (guardEventType == GuardEventType.BEGIN_SHIFT) {
                    currentGuard = guards.computeIfAbsent(guardId, Guard::new);
                }

                Objects.requireNonNull(currentGuard).addEvent(new GuardEvent(timestamp, guardEventType));
            } else {
                throw new IllegalArgumentException();
            }
        }

        return Collections.unmodifiableMap(guards);
    }

    enum GuardEventType {

        BEGIN_SHIFT,
        FALL_ASLEEP,
        WAKE_UP;

        public static GuardEventType fromString(String string) {
            if ("wakes up".equals(string)) {
                return WAKE_UP;
            } else if ("falls asleep".equals(string)) {
                return FALL_ASLEEP;
            } else if (string.contains("begins shift")) {
                return BEGIN_SHIFT;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    static class GuardEvent implements Comparable<GuardEvent> {

        private final LocalDateTime timestamp;

        private final GuardEventType guardEventType;

        GuardEvent(LocalDateTime timestamp, GuardEventType guardEventType) {
            this.timestamp = timestamp;
            this.guardEventType = guardEventType;
        }

        private LocalDateTime getTimestamp() {
            return timestamp;
        }

        private GuardEventType getGuardEventType() {
            return guardEventType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            GuardEvent that = (GuardEvent) o;

            return new EqualsBuilder()
                    .append(timestamp, that.timestamp)
                    .append(guardEventType, that.guardEventType)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(timestamp)
                    .append(guardEventType)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("timestamp", timestamp)
                    .append("guardEventType", guardEventType)
                    .toString();
        }

        @Override
        public int compareTo(GuardEvent o) {
            return Comparator
                    .comparing(GuardEvent::getTimestamp)
                    .thenComparing(GuardEvent::getGuardEventType)
                    .compare(this, o);
        }
    }

    static class Guard {

        private final int id;

        private final Set<GuardEvent> events = new TreeSet<>();

        Guard(int id) {
            this.id = id;
        }

        void addEvent(GuardEvent event) {
            events.add(event);
        }

        List<Pair<LocalDateTime, Integer>> getSleeps() {
            List<Pair<LocalDateTime, Integer>> sleeps = new ArrayList<>();

            LocalDateTime sleepStart = null;
            for (GuardEvent event : events) {
                switch (event.guardEventType) {
                    case BEGIN_SHIFT:
                        if (sleepStart != null) {
                            throw new IllegalStateException();
                        }
                        break;
                    case FALL_ASLEEP:
                        sleepStart = event.timestamp;
                        break;
                    case WAKE_UP:
                        if (sleepStart == null) {
                            throw new IllegalStateException();
                        } else {
                            sleeps.add(ImmutablePair.of(sleepStart, calculateSleepTimeMinutes(sleepStart, event.timestamp)));
                            sleepStart = null;
                        }
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }

            return sleeps;
        }

        int getSleepTimeMinutes() {
            return getSleeps().stream()
                    .map(Pair::getRight)
                    .mapToInt(Integer::intValue)
                    .sum();
        }

        int countsOfMostSleepyMinute() {
            return getSleepyCountsAtMinute(getMostSleepyMinute());
        }

        int getMostSleepyMinute() {
            int[] minutes = new int[60];

            for (Pair<LocalDateTime, Integer> sleep : getSleeps()) {
                int start = sleep.getLeft().getMinute();
                int end = start + sleep.getRight();

                for (int i = start; i < end; ++i) {
                    minutes[i]++;
                }
            }

            return IntStream.range(0, minutes.length)
                    .reduce((a, b) -> minutes[a] < minutes[b] ? b : a)
                    .orElseThrow();
        }

        int getSleepyCountsAtMinute(int minute) {
            int count = 0;

            for (Pair<LocalDateTime, Integer> sleep : getSleeps()) {
                int start = sleep.getLeft().getMinute();
                int end = start + sleep.getRight();

                if (start <= minute || minute <= end) {
                    ++count;
                }

            }
            return count;
        }

        private int calculateSleepTimeMinutes(LocalDateTime start, LocalDateTime end) {
            return Long.valueOf(Duration.between(start, end).toMinutes()).intValue();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("id", id)
                    .append("events", events)
                    .toString();
        }
    }

}
