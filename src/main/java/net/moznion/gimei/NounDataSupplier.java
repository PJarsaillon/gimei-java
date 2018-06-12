package net.moznion.gimei;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final public class NounDataSupplier {
	private static final NounData NOUN_DATA;

	static {
		try (InputStream in = NounDataSupplier.class.getResourceAsStream("/nouns.yml")) {
		    StringBuilder nounSourceBuilder = new StringBuilder();
			String nounSource;
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    nounSourceBuilder.append(line).append("\n");
                }
                nounSource = nounSourceBuilder.toString();
			}
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			NOUN_DATA = mapper.readValue(nounSource, NounData.class);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load nouns.yml file.");
		}
	}

	public static NounData getNounData() {
		return NOUN_DATA;
	}

	@Data
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class NounData {
		private List<NameUnit> nouns;

		@JsonProperty("nouns")
		public void setNouns(List<List<String>> data) {
			final List<NameUnit> nouns = new ArrayList<>();
			for (List<String> datam : data) {
				nouns.add(new NameUnit(datam));
			}
			this.nouns = Collections.unmodifiableList(nouns);
		}
	}
}
