val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

dependencies {
    add("implementation", libs.findLibrary("koin-core").get())
}