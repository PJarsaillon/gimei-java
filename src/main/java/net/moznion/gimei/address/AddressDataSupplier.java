package net.moznion.gimei.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import net.moznion.gimei.NameUnit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class AddressDataSupplier {
	public static final AddressData ADDRESS_DATA;

	static {
		try (InputStream in = AddressDataSupplier.class.getResourceAsStream("/addresses.yml")) {
			StringBuilder addressSourceBuilder = new StringBuilder();
			String addressDataSource;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null)
				{
					addressSourceBuilder.append(line).append("\n");
				}
				addressDataSource = addressSourceBuilder.toString();
			}
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			ADDRESS_DATA = mapper.readValue(addressDataSource, AddressData.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load addresses.yml file.");
		}
	}

	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	static class AddressData {
		private List<NameUnit> prefectures;
		private List<NameUnit> cities;
		private List<NameUnit> towns;

		@JsonProperty("addresses")
		public void setAll(Map<String, List<List<String>>> data) {
			prefectures = new ArrayList<>();
			for (List<String> prefecture : data.get("prefecture")) {
				prefectures.add(new NameUnit(prefecture));
			}

			cities = new ArrayList<>();
			for (List<String> city : data.get("city")) {
				cities.add(new NameUnit(city));
			}

			towns = new ArrayList<>();
			for (List<String> town : data.get("town")) {
				towns.add(new NameUnit(town));
			}
		}
	}
}
