package org.softlang.company.tests;

import org.softlang.company.xjc.*;

import static org.softlang.company.features.Serialization.*;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.Before;

public class SerializationTest {

	private static String sampleCompany = "inputs" + File.separator
			+ "sampleCompany.xml";
	private Company c;

	@Before
	public void initCompany() throws JAXBException, IOException {
		File sample = new File(sampleCompany);
		c = deserializeCompany(sample);
	}

	@Test
	public void testDeserialization() throws JAXBException, IOException {
		File sample = new File(sampleCompany);
		deserializeCompany(sample);
	}

	@Test
	public void testSerialization() throws JAXBException, XMLStreamException,
			IOException {
		new File("outputs").mkdir();
		File out = new File("outputs" + File.separator + "sampleCompany.tmp");
		serializeCompany(out, c);
		c = deserializeCompany(out);
	}

}