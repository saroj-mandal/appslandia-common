// The MIT License (MIT)
// Copyright © 2015 AppsLandia. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.appslandia.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author <a href="mailto:haducloc13@gmail.com">Loc Ha</a>
 *
 */
public class Jdk8FileUtils {

	public static void deleteRecursively(File root) throws IOException {
		deleteRecursively(root.toPath());
	}

	public static void deleteRecursively(Path root) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				try {
					Files.delete(dir);
				} catch (DirectoryNotEmptyException ex) {
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static void copyRecursively(File src, File dest) throws IOException {
		copyRecursively(src.toPath(), dest.toPath());
	}

	public static void copyRecursively(Path src, Path dest) throws IOException {
		BasicFileAttributes srcAttr = Files.readAttributes(src, BasicFileAttributes.class);

		if (srcAttr.isDirectory()) {
			Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					Files.createDirectories(dest.resolve(src.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.copy(file, dest.resolve(src.relativize(file)));
					return FileVisitResult.CONTINUE;
				}
			});
		} else if (srcAttr.isRegularFile()) {
			Files.copy(src, dest);
		} else {
			throw new IllegalArgumentException("src must denote a directory or file");
		}
	}

	public static String readContent(File f) throws IOException {
		return readContent(f, CharsetUtils.UTF_8);
	}

	public static String readContent(File f, Charset charset) throws IOException {
		return new String(Files.readAllBytes(f.toPath()), charset);
	}
}
