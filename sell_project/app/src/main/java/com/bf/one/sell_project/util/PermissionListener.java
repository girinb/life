package com.bf.one.sell_project.util;

import java.util.List;

public interface PermissionListener {

  void onPermissionGranted();

  void onPermissionDenied(List<String> deniedPermissions);

}
