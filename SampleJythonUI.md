# Introduction #

There are four applications to create : two web applications and two JUnit tests. All application shares the same code with some differences. This page describes how to setup Eclipse project and how to assemble deployment for Web application.

| **Application** | **Description** |
|:----------------|:----------------|
| TestGae | JUnit for GAE based data store |
| TestJPA | Junit for JPA (Apache Derby) data store. All test cases are the same |
| SampleWebGae | Sample Google App Engine application |
| SampleWebTomcat | Sample Tomcat application (Apache Derby data store). Also run in Glassfish container |

# Prerequisites #

| **Package** | **Web page, download** | **Purpose** |
|:------------|:-----------------------|:------------|
| Glassfish | http://glassfish.java.net/ | Open source JEE container |
| Tomcat 7 | http://tomcat.apache.org/ | Web container for not GAE application |
| EclipseLink | http://www.eclipse.org/eclipselink/ | Datastore provider for not GAE application. Also not necessary for Glassfish implementation |
| Guice | http://code.google.com/p/google-guice/ | Lightweight dependency injector for application assembly |
| GIN (GWT Injection) | https://code.google.com/p/google-gin/ | Dependency injector for GWT (client) |
| Objectify | http:///code.google.com/p/objectify-appengine | Lightweight framework for Google App Engine |
| FileUpload | http://commons.apache.org/fileupload/ | Prerequisite for some utilities |
| JavaMail | http://www.oracle.com/technetwork/java/javamail/index.html | Prerequisite for some utilities, necessary for not GAE application |
| Jython | http://www.jython.org/ | Intepreted language for creating business logic outside java |
| Apache Derby | http://db.apache.org/derby/ | Datastore for not GAE application. Not necessary for Glasshish. |


# Details #

Packages names are related to source code http://code.google.com/p/javahotel/source/browse/#svn%2Ftrunk

| **Package** | **Description** | **TestGAE** | **TestJPA** | **SampleWebGae** | **SampleWebTomcat** |
|:------------|:----------------|:------------|:------------|:-----------------|:--------------------|
| Cache/gwtcachegae | MemCache (GAE) implementation of Cache | X |  | X |  |
| GwtUI/gwtuiserver | Server part of GwtUI | X | X | X | X |
| GwtUI/gwtuiclient | Client part of GwtUI |  |  | X | X |
| GwtUI/gwtuishared | Share part of GWTUi | X | X | X | X |
| JythonUI/jythonuiclient | Client part of JythonUI |  |  | X | X |
| JythonUI/jythonuiserver | Server part of JythonUI | X | X | X | X |
| JythonUI/jythonuishare | Shared part of JythonUI | X | X | X | X |
| Sample/Test/sampletestcase | JUnit4 test cases | X | X |  |  |
| Sample/Test/sampletestjpaguice | Guice injector for jpa unit test |  | X |  |  |
| Sample/Test/sampletestgaeguice | Guice injectot for gae unit test | X |  |  |  |
| Sample/WebApp/sampledbgae | GAE implementation of datastore | X |  | X |  |
| Sample/WebApp/sampledbjpa | JPA (EclipseLink) implementation of datastore |  | X |  | X |
| Sample/WebApp/sampledbmodel | DAO (interfaces) for sample datastore | X | X | X | X |
| Sample/WebApp/samplegaeguice | Guice injector for GAE web application |  |  | X |  |
| Sample/WebApp/samplegaewar | Example of war directory for GAE web application |  |  |  |  |
| Sample/WebApp/sampletomcatguice | Guice injector for Tomcat web application |  |  |  | X |
| Sample/WebApp/sampletomcatwar | Example of war directory for Tomcat web application |  |  |  |  |
| Sample/WebApp/samplewebsrc | Web application code |  |  | X | X |
| Test/gaetestenhancer | GAE stubs to execute test code | X |  |  |  |
|  |  |  |  |  |
| EclipseLink |  |  | X |  | X |
| GIN |  |  |  | X | X |
| Guice |  | X | X | X | X |
| Objectify |  | X | X |  |  |
| Fileupload |  | X | X | X | X |
| JavaMail |  |  |  | X | X |
| Jython |  | X | X | X | X |
| Apache Derby |  |  |  | X | X |

# Deployment #

| **Package** | **Jars** | **SampleWebGae** | **SampleWebTomcat** |
|:------------|:---------|:-----------------|:--------------------|
| Guice | guice-3.0.jar aopalliance.jar javax.inject.jar | X | X |
| Objectify | objectify-4.0b1.jar | X |  |
| Jython | jython-standalone-2.5.3.jar | X | X |
| Apache Derby | derby.jar |  | X |
| EclipseLink | eclipselink.jar javax.persistence\_1.0.0.jar |  | X |