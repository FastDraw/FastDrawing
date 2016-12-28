package com.company.andrzej.fastdraw;

import android.hardware.Camera;

class SOSModule {
    private Camera cam;
    public void flash_effect() throws InterruptedException
    {
        cam = Camera.open();
        final Camera.Parameters p = cam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        Thread a = new Thread()
        {
            public void run()
            {
                for(int i =0; i < 10; i++)
                {
                    try {
                        cam.setParameters(p);
                        cam.startPreview();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    cam.stopPreview();

                }
            }
        };
        a.start();
    }}



