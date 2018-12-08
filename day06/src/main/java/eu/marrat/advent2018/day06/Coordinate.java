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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

class Coordinate {

	private final int x;

	private final int y;

	Coordinate() {
		this(0, 0);
	}

	Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	Coordinate next(Direction direction) {
		switch (direction) {
			case NORTH:
//				System.out.format("Going %6s from %d,%d to %d,%d %n", direction, x, y, x, y - 1);
				return new Coordinate(x, y - 1);
			case EAST:
//				System.out.format("Going %6s from %d,%d to %d,%d %n", direction, x, y, x + 1, y);
				return new Coordinate(x + 1, y);
			case SOUTH:
//				System.out.format("Going %6s from %d,%d to %d,%d %n", direction, x, y, x, y + 1);
				return new Coordinate(x, y + 1);
			case WEST:
//				System.out.format("Going %6s from %d,%d to %d,%d %n", direction, x, y, x - 1, y);
				return new Coordinate(x - 1, y);
			default:
				throw new IllegalStateException();
		}
	}

	Distance calculateDistance(OriginalCoordinate other) {
		return new Distance(this, other);
	}

	@Override
	public String toString() {
		return String.format("(%3d, %3d)", x, y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Coordinate that = (Coordinate) o;

		return new EqualsBuilder()
				.append(x, that.x)
				.append(y, that.y)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(x)
				.append(y)
				.toHashCode();
	}
}
