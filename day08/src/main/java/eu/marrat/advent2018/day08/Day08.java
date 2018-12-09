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

package eu.marrat.advent2018.day08;

import eu.marrat.advent2018.common.ClasspathFileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day08 {

	public static void main(String[] args) {
		int[] input = ClasspathFileUtils.getTokensFromSingleLine("input", ' ')
				.mapToInt(Integer::parseInt)
				.toArray();

		Node root = null;
		int currentChildNodes = -1;
		int currentMetaDataEntries = -1;
		Node currentNode = null;
		Stack<Node> stack = new Stack<>();

		for (int i : input) {
			if (currentChildNodes < 0) {
				currentChildNodes = i;
				System.out.format("Set new quantity of child nodes: %d%n", currentChildNodes);
			} else if (currentMetaDataEntries < 0) {
				currentMetaDataEntries = i;
				System.out.format("Set new quantity of metadata entries: %d%n", currentMetaDataEntries);

				currentNode = new Node(currentChildNodes, currentMetaDataEntries);
				System.out.format("Created new node %s with quantity of child nodes %d and quantity of metadata entries %d%n",
						currentNode.name,
						currentChildNodes,
						currentMetaDataEntries);

				if (!stack.empty()) {
					Node parent = stack.peek();
					System.out.format("Add node %s as child to node %s%n",
							currentNode.name,
							parent.name);
					parent.addChild(currentNode);
				} else {
					root = currentNode;
				}

				if (currentNode.hasMissingChildren()) {
					System.out.println("Reset quantities because a new header will begin");
					stack.push(currentNode);
					currentChildNodes = -1;
					currentMetaDataEntries = -1;
				}
			} else if (currentNode.hasMissingMetaData()) {
				System.out.format("Add metadata %d to current node %s%n", i, currentNode.name);
				currentNode.addMetaData(i);
			} else if (!stack.peek().hasMissingChildren() && stack.peek().hasMissingMetaData()) {
				currentNode = stack.pop();
				currentNode.addMetaData(i);
				System.out.format("Add metadata %d to current node %s%n", i, currentNode.name);
			} else {
				currentChildNodes = i;
				currentMetaDataEntries = -1;
				System.out.format("Node %s finished, beginning a new header with quantity of child nodes: %d%n",
						currentNode.name,
						currentChildNodes);
			}
		}

		System.out.println(stack.size());
		System.out.println(Objects.requireNonNull(root).sumMetaData());
	}

	static class Node {

		private final String name;

		private final Node[] children;

		private final int[] metaData;

		Node(int childNodes, int metaDataEntries) {
			name = RandomStringUtils.randomAlphabetic(4);
			children = new Node[childNodes];
			metaData = new int[metaDataEntries];

			Arrays.fill(metaData, -1);
		}

		boolean hasMissingChildren() {
			return Stream.of(children).anyMatch(Objects::isNull);
		}

		boolean hasMissingMetaData() {
			return IntStream.of(metaData).anyMatch(i -> i < 0);
		}

		void addMetaData(int metaData) {
			for (int i = 0; i < this.metaData.length; ++i) {
				if (this.metaData[i] < 0) {
					this.metaData[i] = metaData;
					return;
				}
			}

			throw new IllegalStateException();
		}

		void addChild(Node child) {
			for (int i = 0; i < children.length; ++i) {
				if (children[i] == null) {
					children[i] = child;
					return;
				}
			}

			throw new IllegalStateException();
		}

		int sumMetaData() {
			return IntStream.of(metaData).sum() + Stream.of(children).mapToInt(Node::sumMetaData).sum();
		}

	}

}
