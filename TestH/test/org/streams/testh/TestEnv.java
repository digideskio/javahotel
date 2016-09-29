/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.streams.testh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.hadoop.hbase.client.Connection;
import org.junit.After;
import org.junit.Before;
import org.streams.testh1.TestHR;

abstract public class TestEnv {

	protected Connection conH;
	protected java.sql.Connection conB;

	protected BufferedReader getB(String test) throws IOException {
		ClassLoader classLoader = TestHR.class.getClassLoader();
		InputStream is = classLoader.getResource("resource/" + test + ".txt").openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		return reader;
	}

	protected void runU(String stmt) throws SQLException {
		System.out.println(stmt);
		conB.createStatement().execute(stmt);
	}

	@Before
	public void setup() throws IOException, ClassNotFoundException, SQLException {
		conH = TestHelper.connect();
		conB = TestHelper.getC();
	}

	@After
	public void winddown() throws IOException, SQLException {
		if (conH != null)
			conH.close();
		if (conB != null)
			conB.close();
	}

}