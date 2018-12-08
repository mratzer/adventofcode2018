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

package eu.marrat.advent2018.day07;

import eu.marrat.advent2018.common.ClasspathFileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day07 {

	public static void main(String[] args) {
		List<Requirement> requirements = ClasspathFileUtils.getLines("input")
				.filter(StringUtils::isNotEmpty)
				.map(Requirement::new)
				.collect(Collectors.toList());

		Map<String, Step> steps = new TreeMap<>();

		requirements.forEach(r -> {
			Step stepBefore = steps.computeIfAbsent(r.stepBefore, Step::new);
			Step stepAfter = steps.computeIfAbsent(r.stepAfter, Step::new);

			stepBefore.mustBeFinishedBefore(stepAfter);
		});

		Step fake = new Step("_");

		steps.values().stream()
				.filter(s -> s.predecessors.isEmpty())
				.forEach(fake::mustBeFinishedBefore);

		System.out.println(fake.calculateSequenceFromHere()
				.skip(1)
				.collect(Collectors.joining("")));

	}

	static class Step implements Comparable<Step> {

		private final String name;

		private final Set<Step> predecessors = new TreeSet<>(Comparator.comparing(Step::getName));

		private final Set<Step> successors = new TreeSet<>(Comparator.comparing(Step::getName));

		Step(String name) {
			this.name = Objects.requireNonNull(name);
		}

		String getName() {
			return name;
		}

		void mustBeFinishedBefore(Step other) {
			this.successors.add(other);
			other.predecessors.add(this);
		}

		Set<Step> collect() {
			Set<Step> all = new HashSet<>();

			collect(all);

			return all;
		}

		private void collect(Set<Step> steps) {
			steps.add(this);
			successors.forEach(p -> p.collect(steps));
		}

		Stream<String> calculateSequenceFromHere() {
			Set<Step> openSteps = collect();
			TreeSet<Step> readySteps = new TreeSet<>();
			Set<Step> performedSteps = new LinkedHashSet<>();
			performedSteps.add(this);

			while (!openSteps.isEmpty()) {
				openSteps.stream()
						.filter(s -> s.isReady(performedSteps))
						.forEach(readySteps::add);

				if (!readySteps.isEmpty()) {
					Step next = readySteps.iterator().next();

					performedSteps.add(next);
					openSteps.remove(next);
					readySteps.remove(next);
				}
			}

			return performedSteps.stream()
					.map(Step::getName);
		}

		private boolean isReady(Set<Step> performedSteps) {
			return predecessors.isEmpty() || performedSteps.containsAll(predecessors);
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}

			if (o == null || getClass() != o.getClass()) {
				return false;
			}

			Step step = (Step) o;

			return new EqualsBuilder()
					.append(name, step.name)
					.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					.append(name)
					.toHashCode();
		}

		@Override
		public int compareTo(Step o) {
			return name.compareTo(o.name);
		}
	}

	static class Requirement {

		private static final Pattern PATTERN = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin\\.");

		private final String stepBefore;

		private final String stepAfter;

		Requirement(String string) {
			Matcher matcher = PATTERN.matcher(string);

			if (matcher.matches()) {
				stepBefore = matcher.group(1);
				stepAfter = matcher.group(2);
			} else {
				throw new IllegalArgumentException();
			}
		}

	}

}
