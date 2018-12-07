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

package eu.marrat.advent2018.day05;

import eu.marrat.advent2018.common.ClasspathFileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day05 {

    public static void main(String[] args) {
        List<Integer> input = ClasspathFileUtils.getLines("input")
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .chars()
                .boxed()
                .collect(Collectors.toList());

        LinkedList<Integer> pairs = removeNeighboringPairs(input);
        System.out.println(pairs.size());

        long min = IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> getFilteredList(input, c + 32, c))
                .map(Day05::removeNeighboringPairs)
                .mapToInt(Collection::size)
                .min()
                .orElse(-1);

        System.out.println(min);
    }

    private static List<Integer> getFilteredList(List<Integer> input, int lc, int uc) {
        return input.stream()
                .mapToInt(Integer::intValue)
                .filter(i -> i != lc && i != uc)
                .boxed()
                .collect(Collectors.toList());
    }

    private static LinkedList<Integer> removeNeighboringPairs(List<Integer> input) {
        LinkedList<Integer> list = new LinkedList<>(input);

        boolean changed;

        do {
            changed = false;

            for (ListIterator<Integer> iterator = list.listIterator(); iterator.hasNext(); ) {
                Integer right = iterator.next();

                // set iterator position to the same as before call of .next()
                iterator.previous();

                if (iterator.hasPrevious()) {
                    Integer left = iterator.previous();

                    if ((right - left == 32) || (left - right == 32)) {
                        // remove left
                        iterator.remove();

                        // remove right
                        iterator.next();
                        iterator.remove();
                        changed = true;
                    } else {
                        // set iterator position to the same as before call of .previous()
                        iterator.next();
                    }

                }

                // set iterator position to same as after call of first .next() to continue loop
                if (iterator.hasNext()) {
                    iterator.next();
                }
            }
        } while (changed);

        return list;
    }

}
