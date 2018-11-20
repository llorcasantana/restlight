<h2>
  Restlight-widget
</h2>

<p>
  Restlight-widget es una sub biblioteca de <b>Restlight</b> Basada de la librería <b>Volley.</b> Que facilita la cargar de imágenes através de la red, y también para almacenarlas en la memoria caché de su teléfono <b>Android</b>.  
</p>
<p>
  <a href="https://github.com/JesusBetaX/Restlight/raw/master/dist">Descargar .jars</a>
</p>

## Usando NetworkImageView

Restlight a incluido una biblioteca extremadamente útil llamado NetworkImageView. NetworkImageView está destinado a ser un reemplazo directo para el ImageView estándar en escenarios cuando su recurso de imagen proviene de una ubicación de red (es decir, URL) en lugar de un recurso estático.

```
<ImageView
    android:id="@+id/twitter_avatar"
    android:layout_width="48dp"
    android:layout_height="48dp"
    android:layout_alignParentBottom="true"
    android:layout_alignParentTop="true"
    android:layout_marginRight="6dip"
 />
```
Se convierte
```
<restlight.widget.NetworkImageView
    android:id="@+id/twitter_avatar"
    android:layout_width="48dp"
    android:layout_height="48dp"
    android:layout_alignParentBottom="true"
    android:layout_alignParentTop="true"
    android:layout_marginRight="6dip"
 />
```

El siguiente paso es llenar tu elemento programáticamente usando la URL de la imagen. Esto es sencillo también, al menos a primera vista:
```
NetworkImageView avatar = (NetworkImageView)view.findViewById(R.id.twitter_avatar);
avatar.setImageUrl("http://someurl.com/image.png",mImageLoader);
```

## Configurando el ImageLoader

La clase ImageLoader es realmente responsable de configurar el almacenamiento en caché en el dispositivo, y la inicialización del objeto no es trivial. Para comenzar con el ImageLoader, comience por declarar variables privadas para su uso en su clase:

**Patrón Singleton**:

Se recomienda que utilice un objeto ImageLoader común para su aplicación en forma de Singleton.

```
import java.util.concurrent.Executor;

import restlight.Restlight;
import restlight.widget.ImageLoader;
import restlight.widget.LruImageCache;

public final class WebService {

  private static WebService instance;
  
  private final Restlight mRestlight;
  pricate final ImageLoader mImageLoader; 
  
  private WebService() {  
    mRestlight = Restlight.getInstance();

    LruImageCache mLruImageCache = new LruImageCache();
    mImageLoader = new ImageLoader(mRestlight.getQueue(), mLruImageCache); 
  }
  
  public Restlight api() {
    return mRestlight;
  }

  public ImageLoader getImageLoader() {
    return mImageLoader;
  }
  
  public static WebService getInstance() {
    if (instance == null)  instance = new WebService();
    return instance;
  }
}
```
Usar Singleton es tan simple como:

```
ImageLoader mImageLoader = WebService.getInstance().getImageLoader();
```
