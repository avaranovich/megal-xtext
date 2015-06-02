package plugins.jaxb;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.softlang.megal.mi2.api.Artifact;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import plugins.root.elementof.Acceptor;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;

public class AcceptXML extends Acceptor {
	@Override
	public Optional<String> accept(Artifact artifact) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		try (InputStream stream = artifact.getBytes().openStream()) {
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();

			reader.parse(new InputSource(stream));
			return Optional.absent();
		} catch (IOException | SAXException | ParserConfigurationException e) {
			return Optional.of(Throwables.getStackTraceAsString(e));
		}
	}
}
