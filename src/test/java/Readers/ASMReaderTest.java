package Readers;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ASMReaderTest {

	ASMReader reader;

	@Test
	public void testEmptyInput() {
		reader = new ASMReader();
		assertEquals(0, reader.getClassNodeWrappers(new LinkedList<String>(), new LinkedList<InputStream>()).size());
	}

	@Test
	public void testNonExistantClassName() {
		List<String> invalidNames = new LinkedList<String>();
		invalidNames.add("java.scooby.doo");
		reader = new ASMReader();
		assertEquals(0, reader.getClassNodeWrappers(invalidNames, new LinkedList<InputStream>()).size());
	}

	@Test
	public void testOneExistantClassName() {
		List<String> validNames = new LinkedList<String>();
		validNames.add("java.lang.Iterable");
		reader = new ASMReader();
		assertEquals(1, reader.getClassNodeWrappers(validNames, new LinkedList<InputStream>()).size());
	}

	@Test
	public void testOneValidOneInvalidClassName() {
		List<String> names = new LinkedList<String>();
		names.add("java.lang.Iterable");
		names.add("java.scooby.doo");
		reader = new ASMReader();
		assertEquals(1, reader.getClassNodeWrappers(names, new LinkedList<InputStream>()).size());
	}
}
