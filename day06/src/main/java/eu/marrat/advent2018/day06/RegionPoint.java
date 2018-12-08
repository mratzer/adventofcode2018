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

import java.awt.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

class RegionPoint {

	private final Coordinate ownCoordinate;

	private final OriginalData originalData;

	private final List<Distance> closestCoordinates;

	private final Map<Direction, RegionPoint> neighbors = new EnumMap<>(Direction.class);

	RegionPoint(Coordinate ownCoordinate, OriginalData originalData) {
		this.ownCoordinate = ownCoordinate;
		this.originalData = originalData;

		closestCoordinates = getShortestDistances(ownCoordinate, originalData);
	}

	Coordinate getOwnCoordinate() {
		return ownCoordinate;
	}

	boolean hasUniqueClosestDistance() {
		return closestCoordinates.size() == 1;
	}

	int getSumOfAllDistances() {
		return originalData.getAllCoordinates().stream()
				.map(ownCoordinate::calculateDistance)
				.mapToInt(Distance::getDistance)
				.sum();
	}

	OriginalCoordinate getClosestNamedCoordinate() {
		if (hasUniqueClosestDistance()) {
			return closestCoordinates.get(0).getTo();
		} else {
			throw new IllegalStateException();
		}
	}

	RegionPoint next(Direction direction) {
		return neighbors.computeIfAbsent(direction, this::createNext);
	}

	private RegionPoint createNext(Direction direction) {
		Coordinate next = ownCoordinate.next(direction);
		return new RegionPoint(originalData.findMatchingOriginalCoordinate(next).orElse(next), originalData);
	}

	private List<Distance> getShortestDistances(Coordinate ownCoordinate, OriginalData originalData) {
		return originalData.getAllCoordinates().stream()
				.map(ownCoordinate::calculateDistance)
				.collect(Collectors.groupingBy(Distance::getDistance, TreeMap::new, Collectors.toList()))
				.firstEntry()
				.getValue();
	}

	Color getColor() {
		if (ownCoordinate instanceof OriginalCoordinate) {
			return ((OriginalCoordinate) ownCoordinate).getColor();
		} else if (hasUniqueClosestDistance()) {
			return closestCoordinates.get(0).getTo().getColor().darker();
		} else {
			return Color.BLACK;
		}
	}

}
