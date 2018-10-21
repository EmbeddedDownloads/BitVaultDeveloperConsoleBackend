package org.bitvault.appstore.cloud.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.velocity.app.VelocityEngine;
import org.bitvault.appstore.cloud.constant.APIConstants;
import org.bitvault.appstore.cloud.security.CustomPermissionEvaluator;
import org.bitvault.appstore.cloud.security.RestAuthenticationEntryPoint;
import org.bitvault.appstore.cloud.security.auth.filter.RequestAuthenticationProvider;
import org.bitvault.appstore.cloud.security.auth.filter.RequestLoginProcessingFilter;
import org.bitvault.appstore.cloud.security.auth.jwt.JwtAuthenticationProvider;
import org.bitvault.appstore.cloud.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import org.bitvault.appstore.cloud.security.auth.jwt.SkipPathRequestMatcher;
import org.bitvault.appstore.cloud.security.auth.jwt.extractor.TokenExtractor;
import org.bitvault.appstore.cloud.user.common.service.DevPaymentService;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPAOS11Binding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.trust.httpclient.TLSProtocolConfigurer;
import org.springframework.security.saml.trust.httpclient.TLSProtocolSocketFactory;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfile;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true)

// @EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true,
// prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/rest/api/auth/faraz";
	public static final String FORGET_PASSWORD = APIConstants.AUTH_BASE + APIConstants.FORGET_PASSWORD;
	public static final String RESET_PASSWORD = APIConstants.AUTH_BASE + APIConstants.RESET_PASSWORD;
	public static final String COUNTRY = APIConstants.COMMON_API_BASE + APIConstants.GET_COUNTRIES;
	public static final String COUNTRY_STATE = APIConstants.COMMON_API_BASE + APIConstants.GET_COUNTRY_STATES;
	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/rest/api/**";
	public static final String VERIFY_EMAIL = APIConstants.DEV_API_BASE + APIConstants.VERIFY_EMAIL;

	private Timer backgroundTaskTimer;
	private MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;
	// @Autowired
	// CorsFilter corsFilter;

	// @Autowired
	// CustomUserDetailsService userDetailsService;
	//
	// @Autowired
	// HttpLogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private CustomPermissionEvaluator permissionEvaluator;

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Autowired
	private AuthenticationFailureHandler failureHandler;

	@Autowired
	private RequestAuthenticationProvider ajaxAuthenticationProvider;

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private TokenExtractor tokenExtractor;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DevPaymentService devPaymentService;

	protected RequestLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
		RequestLoginProcessingFilter filter = new RequestLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT,
				successHandler, failureHandler, objectMapper);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
		List<String> pathsToSkip = Arrays.asList(VERIFY_EMAIL, COUNTRY, COUNTRY_STATE, RESET_PASSWORD, FORGET_PASSWORD,
				APIConstants.AUTH_BASE + APIConstants.GET_ACCESS_TOKEN, FORM_BASED_LOGIN_ENTRY_POINT,
				APIConstants.MOBILE_API_BASE + "/**", "/samlToken", "/access/token", "/sso-logout");
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
		JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
				tokenExtractor, matcher, devPaymentService);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);

		/*
		 * auth.inMemoryAuthentication() //
		 * .withUser("user").password("password").authorities("ROLE_USER") // .and() //
		 * .withUser("admin").password("password").authorities("ROLE_DEVELOPER");
		 */

		// auth.userDetailsService(userDetailsService);
		// auth.authenticationProvider(authenticationProvider());

	}

	@PostConstruct
	public void init() {
		this.backgroundTaskTimer = new Timer(true);
		this.multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
	}

	@PreDestroy
	public void destroy() {
		this.backgroundTaskTimer.purge();
		this.backgroundTaskTimer.cancel();
		this.multiThreadedHttpConnectionManager.shutdown();
	}

	@Autowired
	private SAMLUserDetailsServiceImpl samlUserDetailsServiceImpl;

	// Initialization of the velocity engine
	@Bean
	public VelocityEngine velocityEngine() {
		return VelocityFactory.getEngine();
	}

	// XML parser pool needed for OpenSAML parsing
	@Bean(initMethod = "initialize")
	public StaticBasicParserPool parserPool() {
		return new StaticBasicParserPool();
	}

	@Bean(name = "parserPoolHolder")
	public ParserPoolHolder parserPoolHolder() {
		return new ParserPoolHolder();
	}

	// Bindings, encoders and decoders used for creating and parsing messages
	@Bean
	public HttpClient httpClient() {
		return new HttpClient(this.multiThreadedHttpConnectionManager);
	}

	// SAML Authentication Provider responsible for validating of received SAML
	// messages
	@Bean
	public SAMLAuthenticationProvider samlAuthenticationProvider() {
		SAMLAuthenticationProvider samlAuthenticationProvider = new SAMLAuthenticationProvider();
		samlAuthenticationProvider.setUserDetails(samlUserDetailsServiceImpl);
		samlAuthenticationProvider.setForcePrincipalAsString(false);
		return samlAuthenticationProvider;
	}

	// Provider of default SAML Context
	@Bean
	public SAMLContextProviderImpl contextProvider() {
		return new SAMLContextProviderImpl();
	}

	// Initialization of OpenSAML library
	@Bean
	public static SAMLBootstrap sAMLBootstrap() {
		return new SAMLBootstrap();
	}

	// Logger for SAML messages and events
	@Bean
	public SAMLDefaultLogger samlLogger() {
		return new SAMLDefaultLogger();
	}

	// SAML 2.0 WebSSO Assertion Consumer
	@Bean
	public WebSSOProfileConsumer webSSOprofileConsumer() {
		return new WebSSOProfileConsumerImpl();
	}

	// SAML 2.0 Holder-of-Key WebSSO Assertion Consumer
	@Bean
	public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 Web SSO profile
	@Bean
	public WebSSOProfile webSSOprofile() {
		return new WebSSOProfileImpl();
	}

	// SAML 2.0 Holder-of-Key Web SSO profile
	@Bean
	public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
		return new WebSSOProfileConsumerHoKImpl();
	}

	// SAML 2.0 ECP profile
	@Bean
	public WebSSOProfileECPImpl ecpprofile() {
		return new WebSSOProfileECPImpl();
	}

	@Bean
	public SingleLogoutProfile logoutprofile() {
		return new SingleLogoutProfileImpl();
	}

	// Central storage of cryptographic keys
	@Bean
	public KeyManager keyManager() {
		DefaultResourceLoader loader = new DefaultResourceLoader();
		Resource storeFile = loader.getResource("classpath:saml/keyStore/keystore_local.jks");
		// String storePass = "nalle123";
		// Map<String, String> passwords = new HashMap<SAPIConstants.DEV_API_BASE +
		// "/samlToken"tring, String>();
		// passwords.put("apollo", "nalle123");
		// String defaultKey = "apollo";

		String storePass = "spring";
		Map<String, String> passwords = new HashMap<String, String>();
		// passwords.put("com:spring:spring:sp", "spring");
		// String defaultKey = "com:spring:spring:sp";
		passwords.put("com:spring:spring:faraz", "spring");
		String defaultKey = "com:spring:spring:faraz";

		return new JKSKeyManager(storeFile, storePass, passwords, defaultKey);
	}

	// Setup TLS Socket Factory
	@Bean
	public TLSProtocolConfigurer tlsProtocolConfigurer() {
		return new TLSProtocolConfigurer();
	}

	@Bean
	public ProtocolSocketFactory socketFactory() {
		return new TLSProtocolSocketFactory(keyManager(), null, "default");
	}

	@Bean
	public Protocol socketFactoryProtocol() {
		return new Protocol("https", socketFactory(), 443);
	}

	@Bean
	public MethodInvokingFactoryBean socketFactoryInitialization() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setTargetClass(Protocol.class);
		methodInvokingFactoryBean.setTargetMethod("registerProtocol");
		Object[] args = { "https", socketFactoryProtocol() };
		methodInvokingFactoryBean.setArguments(args);
		return methodInvokingFactoryBean;
	}

	@Bean
	public WebSSOProfileOptions defaultWebSSOProfileOptions() {
		WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
		webSSOProfileOptions.setIncludeScoping(false);
		return webSSOProfileOptions;
	}

	// Entry point to initialize authentication, default values taken from
	// properties file
	@Bean
	public SAMLEntryPoint samlEntryPoint() {
		SAMLEntryPoint samlEntryPoint = new SAMLEntryPoint();
		samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
		return samlEntryPoint;
	}

	// Setup advanced info about metadata
	@Bean
	public ExtendedMetadata extendedMetadata() {
		ExtendedMetadata extendedMetadata = new ExtendedMetadata();
		extendedMetadata.setIdpDiscoveryEnabled(true);
		extendedMetadata.setSignMetadata(false);
		extendedMetadata.setEcpEnabled(true);
		return extendedMetadata;
	}

	// IDP Discovery Service
	@Bean
	public SAMLDiscovery samlIDPDiscovery() {
		SAMLDiscovery idpDiscovery = new SAMLDiscovery();
		idpDiscovery.setIdpSelectionPath("/saml/idpSelection");
		return idpDiscovery;
	}

	@Bean
	@Qualifier("idp-keycloak")
	public ExtendedMetadataDelegate ssoCircleExtendedMetadataProvider() throws MetadataProviderException {
		// String idpSSOCircleMetadataURL = "https://idp.ssocircle.com/idp-meta.xml";
		// HTTPMetadataProvider httpMetadataProvider = new
		// HTTPMetadataProvider(this.backgroundTaskTimer, httpClient(),
		// idpSSOCircleMetadataURL);
		// httpMetadataProvider.setParserPool(parserPool());
		// ExtendedMetadataDelegate extendedMetadataDelegate = new
		// ExtendedMetadataDelegate(httpMetadataProvider,
		// extendedMetadata());
		// extendedMetadataDelegate.setMetadataTrustCheck(true);
		// extendedMetadataDelegate.setMetadataRequireSignature(false);
		// backgroundTaskTimer.purge();
		// return extendedMetadataDelegate;

		File metadata = null;
		try {
			DefaultResourceLoader loader = new DefaultResourceLoader();
			Resource storeFile = loader.getResource("classpath:saml/metadata/idp-metadata_local.xml");
			metadata = storeFile.getFile();
			// metadata = new File("saml/metadata/idp-metadata_staging.xml");
		} catch (Exception e) {
			e.printStackTrace();

		}
		FilesystemMetadataProvider provider = null;
		provider = new FilesystemMetadataProvider(metadata);
		provider.setParserPool(ParserPoolHolder.getPool());
		ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(provider, extendedMetadata());
		extendedMetadataDelegate.setMetadataTrustCheck(true);
		extendedMetadataDelegate.setMetadataRequireSignature(false);
		return extendedMetadataDelegate;
	}

	// IDP Metadata configuration - paths to metadata of IDPs in circle of trust
	// is here
	// Do no forget to call iniitalize method on providers
	@Bean
	@Qualifier("metadata")
	public CachingMetadataManager metadata() throws MetadataProviderException {
		List<MetadataProvider> providers = new ArrayList<MetadataProvider>();
		providers.add(ssoCircleExtendedMetadataProvider());
		return new CachingMetadataManager(providers);
	}

	// Filter automatically generates default SP metadata
	@Bean
	public MetadataGenerator metadataGenerator() {
		MetadataGenerator metadataGenerator = new MetadataGenerator();
		// metadataGenerator.setEntityId("com:spring:spring:sp");
		metadataGenerator.setEntityId("com:spring:spring:faraz");

		metadataGenerator.setExtendedMetadata(extendedMetadata());
		metadataGenerator.setIncludeDiscoveryExtension(false);
		metadataGenerator.setKeyManager(keyManager());
		return metadataGenerator;
	}

	// The filter is waiting for connections on URL suffixed with filterSuffix
	// and presents SP metadata there
	@Bean
	public MetadataDisplayFilter metadataDisplayFilter() {
		return new MetadataDisplayFilter();
	}

	// Handler deciding where to redirect user after successful login
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
		SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successRedirectHandler.setDefaultTargetUrl("/samlToken");
		return successRedirectHandler;
	}

	// Handler deciding where to redirect user after failed login
	@Bean
	public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
		SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
		failureHandler.setUseForward(true);
		failureHandler.setDefaultFailureUrl("/error");
		return failureHandler;
	}

	@Bean
	public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter() throws Exception {
		SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter = new SAMLWebSSOHoKProcessingFilter();
		samlWebSSOHoKProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOHoKProcessingFilter.setAuthenticationManager(authenticationManager());
		samlWebSSOHoKProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return samlWebSSOHoKProcessingFilter;
	}

	// Processing filter for WebSSO profile messages
	@Bean
	public SAMLProcessingFilter samlWebSSOProcessingFilter() throws Exception {
		SAMLProcessingFilter samlWebSSOProcessingFilter = new SAMLProcessingFilter();
		samlWebSSOProcessingFilter.setAuthenticationManager(authenticationManager());
		samlWebSSOProcessingFilter.setAuthenticationSuccessHandler(successRedirectHandler());
		samlWebSSOProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
		return samlWebSSOProcessingFilter;
	}

	@Bean
	public MetadataGeneratorFilter metadataGeneratorFilter() {
		return new MetadataGeneratorFilter(metadataGenerator());
	}

	// Handler for successful logout
	@Bean
	public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
		SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
		successLogoutHandler.setDefaultTargetUrl("/sso-logout");
		return successLogoutHandler;
	}

	// Logout handler terminating local session
	@Bean
	public SecurityContextLogoutHandler logoutHandler() {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.setInvalidateHttpSession(true);
		logoutHandler.setClearAuthentication(true);
		return logoutHandler;
	}

	// Filter processing incoming logout messages
	// First argument determines URL user will be redirected to after successful
	// global logout
	@Bean
	public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
		return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
	}

	// Overrides default logout processing filter with the one processing SAML
	// messages
	@Bean
	public SAMLLogoutFilter samlLogoutFilter() {
		return new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[] { logoutHandler() },
				new LogoutHandler[] { logoutHandler() });
	}

	// Bindings
	private ArtifactResolutionProfile artifactResolutionProfile() {
		final ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
		artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
		return artifactResolutionProfile;
	}

	@Bean
	public HTTPArtifactBinding artifactBinding(ParserPool parserPool, VelocityEngine velocityEngine) {
		return new HTTPArtifactBinding(parserPool, velocityEngine, artifactResolutionProfile());
	}

	@Bean
	public HTTPSOAP11Binding soapBinding() {
		return new HTTPSOAP11Binding(parserPool());
	}

	@Bean
	public HTTPPostBinding httpPostBinding() {
		return new HTTPPostBinding(parserPool(), velocityEngine());
	}

	@Bean
	public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
		return new HTTPRedirectDeflateBinding(parserPool());
	}

	@Bean
	public HTTPSOAP11Binding httpSOAP11Binding() {
		return new HTTPSOAP11Binding(parserPool());
	}

	@Bean
	public HTTPPAOS11Binding httpPAOS11Binding() {
		return new HTTPPAOS11Binding(parserPool());
	}

	// Processor
	@Bean
	public SAMLProcessorImpl processor() {
		Collection<SAMLBinding> bindings = new ArrayList<SAMLBinding>();
		bindings.add(httpRedirectDeflateBinding());
		bindings.add(httpPostBinding());
		bindings.add(artifactBinding(parserPool(), velocityEngine()));
		bindings.add(httpSOAP11Binding());
		bindings.add(httpPAOS11Binding());
		return new SAMLProcessorImpl(bindings);
	}

	/**
	 * Define the security filter chain in order to support SSO Auth by using SAML
	 * 2.0
	 * 
	 * @return Filter chain proxy
	 * @throws Exception
	 */
	@Bean
	public FilterChainProxy samlFilter() throws Exception {

		List<SecurityFilterChain> chains = new ArrayList<SecurityFilterChain>();
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"), samlEntryPoint()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/logout/**"), samlLogoutFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"),
				metadataDisplayFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"),
				samlWebSSOProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSOHoK/**"),
				samlWebSSOHoKProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SingleLogout/**"),
				samlLogoutProcessingFilter()));
		chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/discovery/**"), samlIDPDiscovery()));
		return new FilterChainProxy(chains);
	}

	/**
	 * Returns the authentication manager currently used by Spring. It represents a
	 * bean definition with the aim allow wiring from other classes performing the
	 * Inversion of Control (IoC).
	 * 
	 * @throws Exception
	 */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Defines the web based security configuration.
	 * 
	 * @param http
	 *            It allows configuring web based security for specific http
	 *            requests.
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().authenticationEntryPoint(samlEntryPoint());
		http.csrf().disable();
		http.cors();
		// no session create
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
		http.authorizeRequests().antMatchers(APIConstants.MOBILE_API_BASE + "/**").permitAll()
				.antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
				.antMatchers(APIConstants.AUTH_BASE + APIConstants.GET_ACCESS_TOKEN).permitAll() // Token refresh

				.antMatchers("/access/token").permitAll()// end-point
				.antMatchers("/sso-logout").permitAll().antMatchers("/console").permitAll()
				.antMatchers(VERIFY_EMAIL, FORGET_PASSWORD, RESET_PASSWORD, COUNTRY, COUNTRY_STATE).permitAll()// H2
																												// Console
																												// Dash-board
																												// -
																												// only
																												// for
																												// testing
				// .antMatchers(APIConstants.ADMIN_API_BASE+"/**").access("hasRole('ADMIN')").anyRequest().authenticated()
				.and().authorizeRequests().antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated();
		// // Custom JWT based security filter
		// .and().addFilterBefore(corsFilter, ChannelProcessingFilter.class);

		http.addFilterBefore(metadataGeneratorFilter(), ChannelProcessingFilter.class).addFilterAfter(samlFilter(),
				BasicAuthenticationFilter.class);
		// http.addFilterBefore(samlLogoutFilter(), LogoutFilter.class);

		http.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/error").permitAll()

				.antMatchers("/saml/**").permitAll().anyRequest().authenticated();

		http.logout().logoutSuccessUrl("/sso-logout");
	}

	/**
	 * Sets a custom authentication provider.
	 * 
	 * @param auth
	 *            SecurityBuilder used to create an AuthenticationManager.
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(samlAuthenticationProvider());
		// auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(APIConstants.AUTH_BASE + "/login", APIConstants.DEV_API_BASE + "/user");

		web.expressionHandler(new DefaultWebSecurityExpressionHandler() {
			@Override
			protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
					FilterInvocation fi) {
				WebSecurityExpressionRoot root = (WebSecurityExpressionRoot) super.createSecurityExpressionRoot(
						authentication, fi);
				// root.setDefaultRolePrefix(""); //remove the prefix ROLE_
				// root.setPermissionEvaluator(permissionEvaluator);
				return root;
			}
		});

		// DefaultMethodSecurityExpressionHandler expressionHandler = new
		// DefaultMethodSecurityExpressionHandler();
		// expressionHandler.setPermissionEvaluator(permissionEvaluator);
		// expressionHandler.setApplicationContext(applicationContext);
		//
		// return expressionHandler;

	}
}