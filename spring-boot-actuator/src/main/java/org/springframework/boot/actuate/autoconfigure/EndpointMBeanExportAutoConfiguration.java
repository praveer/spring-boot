/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.EndpointMBeanExportAutoConfiguration.EndpointMBeanExportProperties;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.jmx.EndpointMBeanExporter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * {@link EnableAutoConfiguration Auto-configuration} to enable JMX export for
 * {@link Endpoint}s.
 * 
 * @author Christian Dupuis
 */
@Configuration
@AutoConfigureAfter({ EndpointAutoConfiguration.class })
@ConditionalOnExpression("${endpoints.jmx.enabled:true}")
@EnableConfigurationProperties(EndpointMBeanExportProperties.class)
public class EndpointMBeanExportAutoConfiguration {

	@Autowired
	EndpointMBeanExportProperties properties = new EndpointMBeanExportProperties();

	@Bean
	public EndpointMBeanExporter endpointMBeanExporter() {
		EndpointMBeanExporter mbeanExporter = new EndpointMBeanExporter();

		String domain = this.properties.getDomain();
		if (StringUtils.hasText(domain)) {
			mbeanExporter.setDomain(domain);
		}

		mbeanExporter.setEnsureUniqueRuntimeObjectNames(this.properties.getUniqueNames());
		mbeanExporter.setObjectNameStaticProperties(this.properties.getStaticNames());

		return mbeanExporter;
	}

	@ConfigurationProperties(name = "endpoints.jmx")
	public static class EndpointMBeanExportProperties {

		private String domain;

		private boolean uniqueNames = false;

		private Properties staticNames = new Properties();

		public String getDomain() {
			return this.domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public boolean getUniqueNames() {
			return this.uniqueNames;
		}

		public void setUniqueNames(boolean uniqueNames) {
			this.uniqueNames = uniqueNames;
		}

		public Properties getStaticNames() {
			return this.staticNames;
		}

		public void setStaticNames(String[] staticNames) {
			this.staticNames = StringUtils.splitArrayElementsIntoProperties(staticNames,
					"=");
		}
	}

}