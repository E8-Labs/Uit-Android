package com.antizon.uit_android.utilities.cache;


import android.content.Context;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.RenderersFactory;

public class PlayerUtilities {

    public static boolean useExtensionRenderers() {
        return false;
    }

    public static RenderersFactory buildRenderersFactory(Context context, boolean preferExtensionRenderer) {@DefaultRenderersFactory.ExtensionRendererMode
        int extensionRendererMode = useExtensionRenderers() ? (preferExtensionRenderer ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON) : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
        return new DefaultRenderersFactory(context.getApplicationContext()).setExtensionRendererMode(extensionRendererMode);
    }
}