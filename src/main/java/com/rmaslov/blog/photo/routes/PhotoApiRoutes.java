package com.rmaslov.blog.photo.routes;

import com.rmaslov.blog.base.routers.BaseApiRoutes;

public class PhotoApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/photo";
    public static final String BY_ID = ROOT + "/{id}";

    public static final String DOWNLOAD = "/photos/{id}";


}
