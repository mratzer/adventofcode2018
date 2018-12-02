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

package eu.marrat.advent2018.day02;

import eu.marrat.advent2018.common.ClasspathFileUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day02 {

	public static void main(String[] args) {
		List<Id> ids = ClasspathFileUtils.getLines("input")
				.map(Id::new)
				.filter(Id::hasAnything)
				.collect(Collectors.toList());

		long twos = ids.stream()
				.filter(Id::hasTwo)
				.count();

		long threes = ids.stream()
				.filter(Id::hasThree)
				.count();

		System.out.println(twos * threes);
	}

	static class Id {

		private final boolean hasTwo;

		private final boolean hasThree;

		Id(String string) {
			Set<Long> twosAndThrees = string.chars()
					.boxed()
					.collect(Collectors.groupingBy(i -> i, Collectors.counting()))
					.values().stream()
					.filter(l -> l == 2 || l == 3)
					.collect(Collectors.toSet());

			hasTwo = twosAndThrees.contains(2L);
			hasThree = twosAndThrees.contains(3L);
		}

		boolean hasTwo() {
			return hasTwo;
		}

		boolean hasThree() {
			return hasThree;
		}

		boolean hasAnything() {
			return hasTwo || hasThree;
		}

	}

}
