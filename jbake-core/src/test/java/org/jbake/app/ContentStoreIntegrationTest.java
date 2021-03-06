package org.jbake.app;

import org.jbake.TestUtils;
import org.jbake.app.configuration.ConfigUtil;
import org.jbake.app.configuration.DefaultJBakeConfiguration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.net.URL;

public abstract class ContentStoreIntegrationTest {

    protected static ContentStore db;
    protected static DefaultJBakeConfiguration config;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    protected static File sourceFolder;

    @BeforeClass
    public static void setUpClass() throws Exception {

        URL sourceUrl = TestUtils.class.getResource("/fixture");

        sourceFolder = new File(sourceUrl.getFile());
        Assert.assertTrue("Cannot find sample data structure!", sourceFolder.exists());

        config = (DefaultJBakeConfiguration) new ConfigUtil().loadConfig(sourceFolder);
        config.setSourceFolder(sourceFolder);

        Assert.assertEquals(".html", config.getOutputExtension());
        config.setDatabaseStore("memory");
        config.setDatabasePath("documents" + System.currentTimeMillis());

        db = DBUtil.createDataStore(config);
    }

    @AfterClass
    public static void cleanUpClass() {
        db.close();
        db.shutdown();
    }

    @Before
    public void setUp() {
        db.startup();
    }

    @After
    public void tearDown() {
        db.drop();
    }
}
