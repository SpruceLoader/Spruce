# Spruce's Trunk

Trunk is the SpruceLoader service responsible for launching/initializing SpruceLoader.

## Structure

Trunk is structured as such:
- One of the [Entrypoints](#entrypoints), which are initialization vectors for the Trunk libray, is loaded and called.
- The [TrunkClassLoader](./src/main/java/xyz/spruceloader/trunk/TrunkClassLoader.java) is created and used to load the
  Trunk library.
- The [ServiceLookup](./src/main/java/xyz/spruceloader/trunk/ServiceLookup.java) class, which is responsible for
  finding and loading the available Trunk services by doing multiple passes.
- 

## Entrypoints

### [Classpath](./src/main/java/xyz/spruceloader/trunk/entrypoint/classpath/TrunkMain.java)

The classpath entrypoint is the default entrypoint for Trunk. It provides a Java `static void main(String[])` method and
catches the provided arguments.  
It also creates and uses a custom ClassLoader to implement its [Transformation Service](. 