package ch.sfdr.fractals;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * "Test" for Version
 * @author D.Ritz
 */
public class VersionTest
{
	@Test
	public void testGetVersion()
	{
		assertTrue(Version.getVersion().contains("Fractals-"));
	}
}
