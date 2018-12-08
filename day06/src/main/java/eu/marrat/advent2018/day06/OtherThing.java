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

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class OtherThing {

	private final List<OriginalCoordinate> allCoordinates;

	private final Map<Direction, OriginalCoordinate> borders;

	OtherThing(List<OriginalCoordinate> allCoordinates) {
		this.allCoordinates = List.copyOf(allCoordinates);
		this.borders = Collections.unmodifiableMap(getBorders(this.allCoordinates));
	}

	List<OriginalCoordinate> getAllCoordinates() {
		return allCoordinates;
	}

	Map<Direction, OriginalCoordinate> getBorders() {
		return borders;
	}

	Optional<Coordinate> findMatchingNamedCoordinate(Coordinate coordinate) {
		return allCoordinates.stream()
				.filter(nc -> nc.getX() == coordinate.getX() && nc.getY() == coordinate.getY())
				.map(Coordinate.class::cast)
				.findFirst();
	}

	private static Map<Direction, OriginalCoordinate> getBorders(List<OriginalCoordinate> coordinates) {
		Map<Direction, OriginalCoordinate> borders = new EnumMap<>(Direction.class);

		borders.put(Direction.NORTH, coordinates.stream()
				.min(Comparator.comparing(Coordinate::getY))
				.orElseThrow());
		borders.put(Direction.EAST, coordinates.stream()
				.max(Comparator.comparing(Coordinate::getX))
				.orElseThrow());
		borders.put(Direction.SOUTH, coordinates.stream()
				.max(Comparator.comparing(Coordinate::getY))
				.orElseThrow());
		borders.put(Direction.WEST, coordinates.stream()
				.min(Comparator.comparing(Coordinate::getX))
				.orElseThrow());

		return borders;
	}
}
