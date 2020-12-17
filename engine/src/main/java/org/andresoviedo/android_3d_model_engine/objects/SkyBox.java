package org.andresoviedo.android_3d_model_engine.objects;

import android.opengl.GLES20;
import android.util.Log;

import org.andresoviedo.android_3d_model_engine.model.CubeMap;
import org.andresoviedo.android_3d_model_engine.model.Object3DData;
import org.andresoviedo.util.android.ContentUtils;
import org.andresoviedo.util.io.IOUtils;
import org.nnmu.R;

import java.io.IOException;
import java.net.URI;

/**
 * Skyboxes downloaded from:
 * <p>
 * https://learnopengl.com/Advanced-OpenGL/Cubemaps
 * https://github.com/mobialia/jmini3d
 */
public class SkyBox {

    private final static float VERTEX_DATA[] = {
            // positions
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,

            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f
    };

    static {

        for (int i=0; i<VERTEX_DATA.length; i++){
            VERTEX_DATA[i] *= 1500;
        }
    }

    public final Integer[] images;

    private CubeMap cubeMap = null;

    public SkyBox(Integer[] images) throws IOException {
        if (images == null || images.length != 6)
            throw new IllegalArgumentException("skybox must contain exactly 6 faces");
        this.images = images;
        this.cubeMap = getCubeMap();
    }

    public CubeMap getCubeMap() throws IOException {
        if (cubeMap != null) {
            return cubeMap;
        }

        cubeMap = new CubeMap(
                IOUtils.read(ContentUtils.getInputStream(images[0])),
                IOUtils.read(ContentUtils.getInputStream(images[1])),
                IOUtils.read(ContentUtils.getInputStream(images[2])),
                IOUtils.read(ContentUtils.getInputStream(images[3])),
                IOUtils.read(ContentUtils.getInputStream(images[4])),
                IOUtils.read(ContentUtils.getInputStream(images[5])));

        return cubeMap;
    }

    /**
     * skybox downloaded from https://github.com/mobialia/jmini3d
     *
     * @return
     */
    public static SkyBox[] getSkyBoxes() {
        try {
            return new SkyBox[]
                    {
                            new SkyBox(new Integer[]{
                                    R.drawable.urban_px,
                                    R.drawable.urban_nx,
                                    R.drawable.urban_py,
                                    R.drawable.urban_ny,
                                    R.drawable.urban_pz,
                                    R.drawable.urban_nz}),

                            new SkyBox(new Integer[]{
                                    R.drawable.posx,
                                    R.drawable.negx,
                                    R.drawable.posy,
                                    R.drawable.negy,
                                    R.drawable.posz,
                                    R.drawable.negz}),
                    };
                    // URI.create("android://org.andresoviedo.dddmodel2/res/drawable/posx.png"),
                    // URI.create("android://org.andresoviedo.dddmodel2/res/drawable/negx.png"),
                    //URI.create("android://org.andresoviedo.dddmodel2/res/drawable/posy.png"),
                    //URI.create("android://org.andresoviedo.dddmodel2/res/drawable/negy.png"),
                    //URI.create("android://org.andresoviedo.dddmodel2/res/drawable/posz.png"),
                    //URI.create("android://org.andresoviedo.dddmodel2/res/drawable/negz.png")});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * skybox downloaded from https://learnopengl.com/Advanced-OpenGL/Cubemaps
     *
     * @return
     */
    public static Object3DData build(SkyBox skyBox) throws IOException {

        Object3DData ret = new Object3DData(IOUtils.createFloatBuffer(VERTEX_DATA.length).put(VERTEX_DATA)).setId("skybox");
        ret.setDrawMode(GLES20.GL_TRIANGLES);
        ret.getMaterial().setTextureId(skyBox.getCubeMap().getTextureId());


        Log.i("SkyBox", "Skybox : " + ret.getDimensions());

        return ret;
    }
}