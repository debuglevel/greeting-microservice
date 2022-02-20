package de.debuglevel.greeter.greeting

//@MicronautTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class GreetingEndpointTests {
//    @Inject
//    lateinit var greetingBlockingStub: GreetingGrpc.GreetingBlockingStub
//
//    @ParameterizedTest
//    @MethodSource("validNameAndLanguageProvider")
//    fun `greet valid names in various languages`(testData: TestDataProvider.NameTestData) {
//        // Assume
//        // the gRPC method wants a non-null language; therefore skip a test if language is null
//        Assumptions.assumeThat(testData.language).isNotNull
//
//        // Arrange
//        val greetRequest = GreetRequest.newBuilder()
//            .setName(testData.name)
//            .setLocale(testData.language)
//            .build()
//
//        // Act
//        val greetReply = greetingBlockingStub.greet(greetRequest)
//
//        // Assert
//        Assertions.assertThat(greetReply.message).contains(testData.expected)
//    }
//
//    fun validNameAndLanguageProvider() = TestDataProvider.validNameAndLanguageProvider()
//
//    // do not test "invalid" names (i.e. "" and " ") as their HTTP call would translate to "/greetings/" which then returns the getList() stuff
////    @ParameterizedTest
////    @MethodSource("invalidNameProvider")
////    fun `greet invalid names`(testData: TestDataProvider.NameTestData) {
////        // Arrange
////
////        // Act
////        val greeting = httpClient.toBlocking().retrieve("/${testData.name}?language=${testData.language}")
////
////        // Assert
////        Assertions.assertThat(greeting).contains("500")
////    }
////
////    fun invalidNameProvider(): Stream<TestDataProvider.NameTestData> {
////        return TestDataProvider.invalidNameProvider()
////    }
//}