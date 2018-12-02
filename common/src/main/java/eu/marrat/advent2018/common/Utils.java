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

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

	public static Path getPathFromClasspathFile(String file) {
		try {
			return Paths.get(Utils.class.getClassLoader().getResource("input").toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private Utils() {
		throw new IllegalStateException();
	}

}
