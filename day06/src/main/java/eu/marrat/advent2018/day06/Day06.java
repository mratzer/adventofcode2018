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

import eu.marrat.advent2018.common.ClasspathFileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day06 {

	public static void main(String[] args) throws IOException {
		List<OriginalCoordinate> coordinates = ClasspathFileUtils.getLines("input")
				.filter(StringUtils::isNotEmpty)
				.map(OriginalCoordinate::fromString)
				.collect(Collectors.toList());

		OtherThing otherThing = new OtherThing(coordinates);

		Thing thing = new Thing(new Coordinate(-500, -500), otherThing);

		BufferedImage image = new BufferedImage(1500, 1500, BufferedImage.TYPE_INT_ARGB);

		Set<OriginalCoordinate> infinites = new HashSet<>();
		Set<Thing> allUnqiue = new HashSet<>();
		Set<Thing> all = new HashSet<>();

		for (int y = 0; y < 1500; ++y) {
			Thing blah = thing;

			for (int x = 0; x < 1500; ++x) {
				all.add(blah);
				if (blah.hasUniqueClosestDistance()) {
					if (y == 0 || y == 1499 || x == 0 || x == 1499) {
						infinites.add(blah.getClosestNamedCoordinate());
					} else {
						allUnqiue.add(blah);
					}
				}

				image.setRGB(x, y, blah.getColor().getRGB());
				blah = blah.next(Direction.EAST);
			}

			thing = thing.next(Direction.SOUTH);
		}

		allUnqiue.removeIf(t -> infinites.contains(t.getClosestNamedCoordinate()));

		Map<OriginalCoordinate, List<Thing>> collect = allUnqiue.stream()
				.collect(Collectors.groupingBy(Thing::getClosestNamedCoordinate));

		int max = collect.values().stream()
				.mapToInt(List::size)
				.max()
				.orElseThrow();

		System.out.println(max);

		List<Thing> collect1 = all.stream()
				.filter(t -> t.getSumOfAllDistances() < 10_000)
				.collect(Collectors.toList());

		collect1.forEach(t -> {
			int x = t.getOwnCoordinate().getX() + 500;
			int y = t.getOwnCoordinate().getY() + 500;

			image.setRGB(x, y, new Color(image.getRGB(x, y)).brighter().brighter().getRGB());
		});

		System.out.println(collect1.size());

		ImageIO.write(image, "png", Paths.get("test.png").toFile());
	}

	static class Thing {

		private final Coordinate ownCoordinate;

		private final OtherThing otherThing;

		private final List<Distance> closestCoordinates;

		private final Map<Direction, Thing> neighbors = new EnumMap<>(Direction.class);

		Thing(Coordinate ownCoordinate, OtherThing otherThing) {
			this.ownCoordinate = ownCoordinate;
			this.otherThing = otherThing;

			closestCoordinates = getShortestDistances(ownCoordinate, otherThing);
		}

		Coordinate getOwnCoordinate() {
			return ownCoordinate;
		}

		boolean hasUniqueClosestDistance() {
			return closestCoordinates.size() == 1;
		}

		int getSumOfAllDistances() {
			return otherThing.getAllCoordinates().stream()
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

		Thing next(Direction direction) {
			return neighbors.computeIfAbsent(direction, this::createNext);
		}

		private Thing createNext(Direction direction) {
			Coordinate next = ownCoordinate.next(direction);
			return new Thing(otherThing.findMatchingNamedCoordinate(next).orElse(next), otherThing);
		}

		private List<Distance> getShortestDistances(Coordinate ownCoordinate, OtherThing otherThing) {
			return otherThing.getAllCoordinates().stream()
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

}
