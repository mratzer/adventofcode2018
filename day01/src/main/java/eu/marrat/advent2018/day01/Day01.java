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

package eu.marrat.advent2018.day01;

import eu.marrat.advent2018.common.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day01 {

	public static void main(String[] args) throws IOException {
		List<Long> deltas = Files.lines(Utils.getPathFromClasspathFile("input"))
				.map(Long::parseLong)
				.collect(Collectors.toList());

		Set<Long> frequencies = new HashSet<>();

		long currentFrequency = 0;
		boolean found = false;

		while (!found) {
			for (Long delta : deltas) {
				currentFrequency += delta;
				if (!frequencies.add(currentFrequency)) {
					found = true;
					break;
				}
			}
		}

		System.out.println(currentFrequency);
	}

}
