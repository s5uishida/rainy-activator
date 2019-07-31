package io.github.s5uishida.iot.rainy.activator;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.s5uishida.iot.rainy.device.IDevice;
import io.github.s5uishida.iot.rainy.device.cc2650.CC2650;
import io.github.s5uishida.iot.rainy.device.mhz19b.MHZ19B;
import io.github.s5uishida.iot.rainy.util.Config;
import io.github.s5uishida.iot.rainy.util.ConfigParams;

/*
 * @author s5uishida
 *
 */
public class Activator implements BundleActivator {
	private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
	private static final Config config = Config.getInstance();

	private static BundleContext context;

	private List<IDevice> devices = new ArrayList<IDevice>();

	@Override
	public void start(BundleContext context) throws Exception {
		LOG.info("{} {} started.", ConfigParams.NAME, ConfigParams.VERSION);
		if (config.getCC2650()) {
			devices.add(new CC2650(config.getClientID()));
			LOG.info("CC2650 installed.");
		}

		if (config.getMHZ19B()) {
			devices.add(new MHZ19B(config.getClientID()));
			LOG.info("MH-Z19B installed.");
		}

		for (IDevice device : devices) {
			device.start();
			LOG.info("{} started.", device.getClass().getSimpleName());
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		for (IDevice device : devices) {
			device.stop();
			LOG.info("{} stopped.", device.getClass().getSimpleName());
		}
		LOG.info("{} {} stopped.", ConfigParams.NAME, ConfigParams.VERSION);
	}
}