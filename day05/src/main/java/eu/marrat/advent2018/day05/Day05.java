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

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class Day05 {

    public static void main(String[] args) {
        LinkedList<Integer> input = ClasspathFileUtils.getLines("input")
                .filter(StringUtils::isNotEmpty)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .chars()
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));

        boolean changed;

        do {
            changed = false;

            for (ListIterator<Integer> iterator = input.listIterator(); iterator.hasNext(); ) {
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
                iterator.next();
            }
        } while (changed);

        System.out.println(input.size());
    }

}
