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

package eu.marrat.advent2018.common;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ClasspathFileUtils {

	private static final Pattern NEW_LINE = Pattern.compile("\n");

	public static Stream<String> getTokensFromSingleLine(String file, char separator) {
		return getLines(file)
				.filter(StringUtils::isNotEmpty)
				.limit(1)
				.map(s -> StringUtils.split(s, separator))
				.flatMap(Stream::of);
	}

	public static Stream<Long> getLongs(String file) {
		return getLines(file).map(Long::parseLong);
	}

	public static Stream<String> getLines(String file) {
		return getScanner(file)
				.useDelimiter(NEW_LINE)
				.tokens();
	}

	private static Scanner getScanner(String file) {
		return new Scanner(
				Objects.requireNonNull(ClasspathFileUtils.class.getClassLoader().getResourceAsStream(file)),
				StandardCharsets.UTF_8);
	}

	private ClasspathFileUtils() {
		throw new IllegalStateException();
	}

}
