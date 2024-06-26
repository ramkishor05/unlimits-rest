package org.brijframework.json.schema.factories;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface SingletonFactory {
	static final String EXCEPTION = "Exception : ";
	static final String CLASSPATH = "classpath:";
	
	public static final Logger LOGGER = LoggerFactory.getLogger(SingletonFactory.class);
	
	public default String toJson(Object object) {
		ObjectMapper mapper= new ObjectMapper();
		try {
			String writeValueAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
			return writeValueAsString;
		} catch (JsonProcessingException e) {
			LOGGER.error(EXCEPTION, e);
		}
		return null;
	}
	
	public void load(Path file, InputStream inputStream);
	
	public default void write(Path file, List<?> list) {
		try {
			Files.writeString(file, toJson(list), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			LOGGER.error(EXCEPTION, e);
		}
	}
	
	public default URL getConfigDirURLForChild(String folder) {
		String path = getConfigDir();
		try {
			if(path.startsWith(CLASSPATH)) {
				String resourcePath = path.split(CLASSPATH)[1];
				if("/".equals(resourcePath)) {
					resourcePath="";
				}
				System.err.println(resourcePath+ folder);
				return Thread.currentThread().getContextClassLoader().getResource(resourcePath+ folder);
			} else {
				return new File(path +folder).toURI().toURL();
			}
		} catch (MalformedURLException e) {
			LOGGER.error(EXCEPTION, e);
		}                                                                     
		return null;
	}
	
	public default  String getConfigDir() {
		return CLASSPATH+"/";
	}

	public default void init(String root){
		LOGGER.info(this.getClass().getName()+" loading  start");
		try {
			URL resource = getConfigDirURLForChild(root);
			if(resource==null) {
				return;
			}
			URI uri = resource.toURI();
			if("jar".equals(uri.getScheme())){
			    setNewFileSystem(uri);
			    Path path = Paths.get(uri);
			    if(path==null) {
					return;
				}
			    callForLoadFromJarSystem(path);
			} else {
				Path path = Paths.get(uri);
				if(path==null) {
					return;
				}
				callForLoadFromFileSystem(path);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			LOGGER.error(EXCEPTION, e);
		}
		LOGGER.info(this.getClass().getName()+" loading  done");
	}

	public default void callForLoadFromJarSystem(Path path) throws IOException {
		try (Stream<Path>  stream= Files.list(path)){
			stream.forEach(file -> {
				System.out.println("jarpath="+file);
		    	if(file.toString().contains(".")) {
		    		try (InputStream inputStream = file.toUri().toURL().openStream()){
						load(file, inputStream);
					} catch (IOException e) {
						LOGGER.error(EXCEPTION, e);
					}
				} else {
					try {
						callForLoadFromJarSystem(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public default void callForLoadFromFileSystem(Path path) throws IOException {
		try (Stream<Path>  stream= Files.list(path)){
				stream.forEach(file -> {
					if(file.toFile().isDirectory()) {
						try {
							callForLoadFromFileSystem(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try (InputStream inputStream = file.toUri().toURL().openStream()) {
							load(file, inputStream);
						} catch (IOException e) {
							LOGGER.error(EXCEPTION, e);
						}
					}
				
			});
		}
	}
	
	public default void init(String root, String templateId){
		LOGGER.info(this.getClass().getName()+" loading  start");
		try {
			URL resource = getConfigDirURLForChild(root);
			if(resource==null) {
				return;
			}
			URI uri = resource.toURI();
			if("jar".equals(uri.getScheme())){
			    setNewFileSystem(uri);
			    Path path = Paths.get(uri);
			    if(path==null) {
					return;
				}
			    
			    callForLoadFromJarSystem(templateId, path);
			} else {
				Path path = Paths.get(uri);
				if(path==null) {
					return;
				}
				callForLoadFromFileSystem(templateId, path);
			}
		} catch (IOException | URISyntaxException e) {
			LOGGER.error(EXCEPTION, e);
		}
		LOGGER.info(this.getClass().getName()+" loading  done");
	}

	public default void setNewFileSystem(URI uri) throws IOException {
		for (FileSystemProvider provider: FileSystemProvider.installedProviders()) {
		    if (provider.getScheme().equalsIgnoreCase("jar")) {
		        try {
		            provider.getFileSystem(uri);
		        } catch (FileSystemNotFoundException e) {
				    provider.newFileSystem(uri, Collections.emptyMap());
		        }
		    }
		}
	}

	public default void callForLoadFromFileSystem(String templateId, Path path) throws IOException {
		System.out.println("Files.list(path)="+Files.list(path));
		try (Stream<Path>  stream= Files.list(path)){
			stream.forEach(file -> {
				if(file.toFile().isDirectory()) {
					try {
						callForLoadFromFileSystem(templateId,file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try (InputStream inputStream = file.toUri().toURL().openStream()) {
						load(file, inputStream, templateId);
					} catch (IOException e) {
						LOGGER.error(EXCEPTION, e);
					}
				}
			});
		}
	}

	public default void callForLoadFromJarSystem(String templateId, Path path) throws IOException {
		try (Stream<Path>  stream= Files.list(path)){
			stream.forEach(file -> {
		    	if(file.toString().contains(".")) {
		    		try (InputStream inputStream = file.toUri().toURL().openStream()) {
						load(file, inputStream, templateId);
					} catch (IOException e) {
						LOGGER.error(EXCEPTION, e);
					}
				}
			});
		}
	}

	public default void load(Path file, InputStream inputStream, String templateId) {
		load(file,inputStream);
	}

}
