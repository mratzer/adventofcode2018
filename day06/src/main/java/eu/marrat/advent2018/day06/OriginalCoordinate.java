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

import org.apache.commons.lang3.RandomUtils;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OriginalCoordinate extends Coordinate {

	private static final Pattern FROM_STRING_PATTERN = Pattern.compile("(\\d+), (\\d+)");

	private final Color color;

	private OriginalCoordinate(int x, int y) {
		super(x, y);

		color = new Color(
				RandomUtils.nextInt(0, 256),
				RandomUtils.nextInt(0, 256),
				RandomUtils.nextInt(0, 256));
	}

	Color getColor() {
		return color;
	}

	static OriginalCoordinate fromString(String string) {
		Matcher matcher = FROM_STRING_PATTERN.matcher(string);

		if (matcher.matches()) {
			return new OriginalCoordinate(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
		} else {
			throw new IllegalArgumentException();
		}
	}
}
