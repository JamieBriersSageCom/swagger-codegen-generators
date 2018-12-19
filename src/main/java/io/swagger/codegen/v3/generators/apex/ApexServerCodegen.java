package io.swagger.codegen.v3.generators.apex;

import io.swagger.codegen.v3.CodegenType;
import io.swagger.codegen.v3.generators.java.AbstractJavaCodegen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class ApexServerCodegen extends AbstractJavaCodegen {

	private static final String CLASS_PREFIX = "classPrefix";
	private static final String API_VERSION = "apiVersion";
	private static final String BUILD_METHOD = "buildMethod";
	private static final String NAMED_CREDENTIAL = "namedCredential";
	private static final Logger LOGGER = LoggerFactory.getLogger(ApexServerCodegen.class);
	private String classPrefix = "swag";
	private String apiVersion = "39.0";
	private String buildMethod = "sfdx";
	private String namedCredential = classPrefix;
	private String srcPath = "force-app/main/default/";
	protected static final String APEX_SERVER_TEMPLATE_DIRECTORY_NAME = "apexserver";

	public ApexServerCodegen() {
		super();

		testFolder = sourceFolder = srcPath;

		embeddedTemplateDir = templateDir = getTemplateDir();
		outputFolder = "generated-code" + File.separator + "apex";
		apiPackage = "classes";
		modelPackage = "classes";
		testPackage = "force-app.main.default.classes";
		modelNamePrefix = classPrefix;
		dateLibrary = "";

		// apiTemplateFiles.put("cls-meta.mustache", ".cls-meta.xml");
		// apiTestTemplateFiles.put("api_test.mustache", ".cls");
		// apiTestTemplateFiles.put("cls-meta.mustache", ".cls-meta.xml");
		// modelTemplateFiles.put("model.mustache", ".cls");
		// modelTemplateFiles.put("cls-meta.mustache", ".cls-meta.xml");
		// modelTestTemplateFiles.put("model_test.mustache", ".cls");
		// modelTestTemplateFiles.put("cls-meta.mustache", ".cls-meta.xml");

//		cliOptions.add(CliOption.newString(CLASS_PREFIX, "Prefix for generated classes. Set this to avoid overwriting existing classes in your org."));
//		cliOptions.add(CliOption.newString(API_VERSION, "The Metadata API version number to use for components in this package."));
//		cliOptions.add(CliOption.newString(BUILD_METHOD, "The build method for this package."));
//		cliOptions.add(CliOption.newString(NAMED_CREDENTIAL, "The named credential name for the HTTP callouts"));

		// supportingFiles.add(new SupportingFile("Swagger.cls", srcPath + "classes", "Swagger.cls"));
		// supportingFiles.add(new SupportingFile("cls-meta.mustache", srcPath + "classes", "Swagger.cls-meta.xml"));
		// supportingFiles.add(new SupportingFile("SwaggerTest.cls", srcPath + "classes", "SwaggerTest.cls"));
		// supportingFiles.add(new SupportingFile("cls-meta.mustache", srcPath + "classes", "SwaggerTest.cls-meta.xml"));
		// supportingFiles.add(new SupportingFile("SwaggerResponseMock.cls", srcPath + "classes", "SwaggerResponseMock.cls"));
		// supportingFiles.add(new SupportingFile("cls-meta.mustache", srcPath + "classes", "SwaggerResponseMock.cls-meta.xml"));

		typeMapping.put("BigDecimal", "Double");
		typeMapping.put("binary", "String");
		typeMapping.put("ByteArray", "Blob");
		typeMapping.put("date", "Date");
		typeMapping.put("DateTime", "Datetime");
		typeMapping.put("file", "Blob");
		typeMapping.put("float", "Double");
		typeMapping.put("number", "Double");
		typeMapping.put("short", "Integer");
		typeMapping.put("UUID", "String");

		setReservedWordsLowerCase(
				Arrays.asList("abstract", "activate", "and", "any", "array", "as", "asc", "autonomous",
						"begin", "bigdecimal", "blob", "break", "bulk", "by", "byte", "case", "cast",
						"catch", "char", "class", "collect", "commit", "const", "continue",
						"convertcurrency", "date", "decimal", "default", "delete", "desc", "do", "else",
						"end", "enum", "exception", "exit", "export", "extends", "false", "final",
						"finally", "float", "for", "from", "future", "global", "goto", "group", "having",
						"hint", "if", "implements", "import", "inner", "insert", "instanceof", "int",
						"interface", "into", "join", "last_90_days", "last_month", "last_n_days",
						"last_week", "like", "limit", "list", "long", "loop", "map", "merge", "new",
						"next_90_days", "next_month", "next_n_days", "next_week", "not", "null", "nulls",
						"number", "object", "of", "on", "or", "outer", "override", "package", "parallel",
						"pragma", "private", "protected", "public", "retrieve", "return", "returning",
						"rollback", "savepoint", "search", "select", "set", "short", "sort", "stat",
						"static", "super", "switch", "synchronized", "system", "testmethod", "then", "this",
						"this_month", "this_week", "throw", "today", "tolabel", "tomorrow", "transaction",
						"trigger", "true", "try", "type", "undelete", "update", "upsert", "using",
						"virtual", "webservice", "when", "where", "while", "yesterday"
				));

		languageSpecificPrimitives = new HashSet<String>(
				Arrays.asList("Blob", "Boolean", "Date", "Datetime", "Decimal", "Double", "ID",
						"Integer", "Long", "Object", "String", "Time"
				));
	}

	@Override
	public void processOpts() {
		super.processOpts();

		modelTemplateFiles.clear();
		apiTemplateFiles.clear();
		apiTestTemplateFiles.clear();
		modelDocTemplateFiles.clear();
		apiDocTemplateFiles.clear();

		apiTemplateFiles.put("api.mustache", ".cls");


		importMapping.clear();


		if (additionalProperties.containsKey(CLASS_PREFIX)) {
			setClassPrefix((String) additionalProperties.get(CLASS_PREFIX));
		}
		additionalProperties.put(CLASS_PREFIX, classPrefix);

		if (additionalProperties.containsKey(API_VERSION)) {
			setApiVersion(toApiVersion((String) additionalProperties.get(API_VERSION)));
		}
		additionalProperties.put(API_VERSION, apiVersion);

		if (additionalProperties.containsKey(BUILD_METHOD)) {
			setBuildMethod((String)additionalProperties.get(BUILD_METHOD));
		}
		additionalProperties.put(BUILD_METHOD, buildMethod);

		if (additionalProperties.containsKey(NAMED_CREDENTIAL)) {
			setNamedCredential((String)additionalProperties.get(NAMED_CREDENTIAL));
		}
		additionalProperties.put(NAMED_CREDENTIAL, namedCredential);

		postProcessOpts();
	}

	@Override
	public String escapeReservedWord(String name) {
		// Identifiers must start with a letter
		return "r" + super.escapeReservedWord(name);
	}

	@Override
	public String toModelName(String name) {
		String modelName = super.toModelName(name);

		// Max length is 40; save the last 4 for "Test"
		if (modelName.length() > 36) {
			modelName = modelName.substring(0, 36);
		}
		return modelName;
	}

	@Override
	public String getDefaultTemplateDir() {
		return APEX_SERVER_TEMPLATE_DIRECTORY_NAME;
	}


	@Override
	public String escapeQuotationMark(String input) {
		return input.replace("'", "\\'");
	}

	public void setBuildMethod(String buildMethod) {
		if (buildMethod.equals("ant")) {
			this.srcPath = "deploy/";
		} else {
			this.srcPath = "src/";
		}
		testFolder = sourceFolder = srcPath;
		this.buildMethod = buildMethod;
	}

	public void setNamedCredential(String namedCredential) {
		this.namedCredential = namedCredential;
	}

	public void setClassPrefix(String classPrefix) {
		// the best thing we can do without namespacing in Apex
		modelNamePrefix = classPrefix;
		this.classPrefix = classPrefix;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	private String toApiVersion(String apiVersion) {
		if (apiVersion.matches("^\\d{2}(\\.0)?$")) {
			return apiVersion.substring(0, 2) + ".0";
		} else {
			LOGGER.warn(String.format("specified API version is invalid: %s - defaulting to %s", apiVersion, this.apiVersion));
			return this.apiVersion;
		}
	}

	private void postProcessOpts() {
		// supportingFiles.add(
		// new SupportingFile("client.mustache", srcPath + "classes", classPrefix + "Client.cls"));
		// supportingFiles.add(new SupportingFile("cls-meta.mustache", srcPath + "classes",
		// classPrefix + "Client.cls-meta.xml"
		// ));
	}

	@Override
	public String escapeText(String input) {
		if (input == null) {
			return input;
		}

		return input.replace("'", "\\'").replace("\n", "\\n").replace("\r", "\\r");
	}

	@Override
	public String toModelTestFilename(String name) {
		return toModelName(name) + "Test";
	}


	@Override
	public String toApiName(String name) {
		return camelize(classPrefix + super.toApiName(name));
	}


	@Override
	public CodegenType getTag() {
		return CodegenType.SERVER;
	}

	@Override
	public String getName() {
		return "apexserver";
	}

	@Override
	public String getHelp() {
		return "Generates an Apex API server library (beta).";
	}



}


