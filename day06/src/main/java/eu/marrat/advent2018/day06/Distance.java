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

package eu.marrat.advent2018.day06;

class Distance {

	private final Coordinate from;

	private final OriginalCoordinate to;

	private final int distance;

	Distance(Coordinate from, OriginalCoordinate to) {
		this.from = from;
		this.to = to;

		distance = Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
	}

	Coordinate getFrom() {
		return from;
	}

	OriginalCoordinate getTo() {
		return to;
	}

	int getDistance() {
		return distance;
	}

	boolean isShorter(Distance other) {
		return distance > other.distance;
	}

	boolean isSame(Distance other) {
		return distance == other.distance;
	}

	boolean isLonger(Distance other) {
		return distance > other.distance;
	}

	@Override
	public String toString() {
		return String.format("from %s to %s -> %d", from, to, distance);
	}
}
