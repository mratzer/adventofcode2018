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

package eu.marrat.advent2018.day03;

import eu.marrat.advent2018.common.ClasspathFileUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day03 {

    public static void main(String[] args) {
        int[][] arr = new int[1000][1000];

        List<Claim> claims = ClasspathFileUtils.getLines("input")
                .map(Claim::new)
                .collect(Collectors.toList());

        claims.forEach(c -> c.apply(arr));

        int count = 0;

        for (int x = 0; x < arr.length; ++x) {
            for (int y = 0; y < arr[x].length; ++y) {
                if (arr[x][y] > 1) {
                    ++count;
                }
            }
        }

        System.out.println(count);

        claims.stream()
                .filter(c -> c.isUnique(arr))
                .findFirst()
                .ifPresent(System.out::println);
    }

    static class Claim {

        private static final Pattern PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

        private final int id;

        private final int x;

        private final int y;

        private final int width;

        private final int height;

        Claim(String string) {
            Matcher matcher = PATTERN.matcher(string);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(String.format("Illegal claim: [%s]", string));
            }
            id = Integer.parseInt(matcher.group(1));
            x = Integer.parseInt(matcher.group(2));
            y = Integer.parseInt(matcher.group(3));
            width = Integer.parseInt(matcher.group(4));
            height = Integer.parseInt(matcher.group(5));
        }

        void apply(int[][] arr) {
            for (int dx = 0; dx < width; ++dx) {
                for (int dy = 0; dy < height; dy++) {
                    arr[x + dx][y + dy]++;
                }
            }
        }

        boolean isUnique(int[][] arr) {
            for (int dx = 0; dx < width; ++dx) {
                for (int dy = 0; dy < height; dy++) {
                    if (arr[x + dx][y + dy] != 1) {
                        return false;
                    }
                }
            }

            return true;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("id", id)
                    .append("x", x)
                    .append("y", y)
                    .append("width", width)
                    .append("height", height)
                    .toString();
        }
    }

}
