package com.company.andrzej.fastdraw;

import android.hardware.Camera;

class SOSModule {
    private Camera camera;
    private Camera.Parameters params;
    private boolean isFlashOn;

    void blink(final int delay, final int times) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i=0; i < times*2; i++) {
                        if (isFlashOn) {
                            turnOnFlash();
                        } else {
                            turnOnFlash();
                        }
                        Thread.sleep(delay);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        t.start();

    }
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }
    }



