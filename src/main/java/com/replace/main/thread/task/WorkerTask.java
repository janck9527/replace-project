package com.replace.main.thread.task;

import com.alibaba.fastjson2.JSON;

/**
 * 工作任务
 */
public class WorkerTask implements Runnable {
    // 任务编号: 共享资源
    private String id;
    private String workName;

    public String getId() {
        return id;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    // 运行其他线程修改该值,但是该共享资源时线程安装的,该变量共享资源对其他线程可见
    public void setId(String id) {
        this.id = id;
    }

    public WorkerTask() {
        super();
    }

    public WorkerTask(String id) {
        super();
        this.id = id;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(100);
            if (!(this.getWorkName().split("-")[1].equals(this.getId()))) {
                System.err.println("当前任务：" + this.getWorkName().split("-")[1] + "    <id>: " + this.getId() + " | 消息为: " + JSON.toJSON(this) + " 数据异常！");
            } else {
                System.out.println("当前任务：" + this.getId() + " | 消息为: " + JSON.toJSON(this) + " 正常执行！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
