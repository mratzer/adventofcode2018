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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day06 {

	private static final int LENGTH = 400;

	public static void main(String[] args) throws IOException {
		List<OriginalCoordinate> coordinates = ClasspathFileUtils.getLines("input")
				.filter(StringUtils::isNotEmpty)
				.map(OriginalCoordinate::fromString)
				.collect(Collectors.toList());

		OriginalData originalData = new OriginalData(coordinates);

		RegionPoint regionPoint = new RegionPoint(new Coordinate(0, 0), originalData);

		BufferedImage image = new BufferedImage(LENGTH, LENGTH, BufferedImage.TYPE_INT_ARGB);

		Set<OriginalCoordinate> assumedInfiniteRoots = new HashSet<>();
		Set<RegionPoint> regionPointsWithUniqueDistance = new HashSet<>();
		Set<RegionPoint> allRegionPoints = new HashSet<>();

		for (int y = 0; y < LENGTH; ++y) {
			RegionPoint current = regionPoint;

			for (int x = 0; x < LENGTH; ++x) {
				allRegionPoints.add(current);
				if (current.hasUniqueClosestDistance()) {
					if (y == 0 || y == LENGTH - 1 || x == 0 || x == LENGTH - 1) {
						assumedInfiniteRoots.add(current.getClosestNamedCoordinate());
					} else {
						regionPointsWithUniqueDistance.add(current);
					}
				}

				image.setRGB(x, y, current.getColor().getRGB());
				current = current.next(Direction.EAST);
			}

			regionPoint = regionPoint.next(Direction.SOUTH);
		}

		regionPointsWithUniqueDistance.removeIf(rp -> assumedInfiniteRoots.contains(rp.getClosestNamedCoordinate()));

		Map<OriginalCoordinate, List<RegionPoint>> collect = regionPointsWithUniqueDistance.stream()
				.collect(Collectors.groupingBy(RegionPoint::getClosestNamedCoordinate));

		int max = collect.values().stream()
				.mapToInt(List::size)
				.max()
				.orElseThrow();

		System.out.println(max);

		List<RegionPoint> regionPointsWithinDistance = allRegionPoints.stream()
				.filter(t -> t.getSumOfAllDistances() < 10_000)
				.collect(Collectors.toList());

		regionPointsWithinDistance.forEach(t -> {
			int x = t.getOwnCoordinate().getX();
			int y = t.getOwnCoordinate().getY();

			image.setRGB(x, y, new Color(image.getRGB(x, y)).brighter().brighter().getRGB());
		});

		System.out.println(regionPointsWithinDistance.size());

		ImageIO.write(image, "png", Paths.get("test.png").toFile());
	}

}
