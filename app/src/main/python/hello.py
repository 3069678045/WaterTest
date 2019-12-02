# coding:utf-8
import numpy as np
import cv2
import os

# 保存图片 截屏一半 获取半屏之后的数字坐标 加上之前的半屏高度 == 数字坐标
def ginseng(imgurl):

    print ("進入python："+imgurl)

    list = []

    BaseDir = os.path.dirname(os.path.abspath(__file__))
    weightsPath = BaseDir+r"/yolov3-voc_40000.weights"  # 模型权重文件
    configPath = BaseDir+r"/yolov3-voc.cfg"  # 模型配置文件
    labelsPath = BaseDir+r"/voc.names"  # 模型类别标签文件

    print ("初始化LABELS:++++++")

    # 初始化一些参数
    LABELS = open(labelsPath).read().strip().split("\n")


    print ("定义变量：")
    positions = []
    boxes = []
    confidences = []
    classIDs = []


    print ("加载网络配置：")
    # 加载 网络配置与训练的权重文件 构建网络
    net = cv2.dnn.readNetFromDarknet(configPath, weightsPath)
    # 读入待检测的图像
    image = cv2.imread(imgurl)
    # 得到图像的高和宽
    (H, W) = image.shape[0:2]

    print ("得到 YOLO需要的输出层")

    # 得到 YOLO需要的输出层
    ln = net.getLayerNames()
    out = net.getUnconnectedOutLayers()  # 得到未连接层得序号  [[200] /n [267]  /n [400] ]
    x = []

    for i in out:  # 1=[200]
        x.append(ln[i[0] - 1])  # i[0]-1    取out中的数字  [200][0]=200  ln(199)= 'yolo_82'
    ln = x

    # ln  =  ['yolo_82', 'yolo_94', 'yolo_106']  得到 YOLO需要的输出层

    # 从输入图像构造一个blob，然后通过加载的模型，给我们提供边界框和相关概率
    # blobFromImage(image, scalefactor=None, size=None, mean=None, swapRB=None, crop=None, ddepth=None)
    print ("blobFromImage: ")
    blob = cv2.dnn.blobFromImage(image, 1 / 255.0, (512, 512), swapRB=True,
                                 crop=False)  # 构造了一个blob图像，对原图像进行了图像的归一化，缩放了尺寸 ，对应训练模型

    net.setInput(blob)  # 将blob设为输入？？？ 具体作用还不是很清楚
    layerOutputs = net.forward(ln)  # ln此时为输出层名称  ，向前传播  得到检测结果

    print ("开始循环：：")

    for output in layerOutputs:  # 对三个输出层 循环
        for detection in output:  # 对每个输出层中的每个检测框循环
            scores = detection[5:]  # detection=[x,y,h,w,c,class1,class2] scores取第6位至最后
            classID = np.argmax(scores)  # np.argmax反馈最大值的索引
            confidence = scores[classID]
            if confidence > 0.8:  # 过滤掉那些置信度较小的检测结果
                box = detection[0:4] * np.array([W, H, W, H])
                print(box)
                (centerX, centerY, width, height) = box.astype("int")
                # 边框的左上角
                x = int(centerX - (width / 2))
                y = int(centerY - (height / 2))

                # 更新检测出来的框
                boxes.append([x, y, int(width), int(height)])
                confidences.append(float(confidence))
                classIDs.append(classID)

    idxs = cv2.dnn.NMSBoxes(boxes, confidences, 0.2, 0.3)
    box_seq = idxs.flatten()  # [ 2  9  7 10  6  5  4]
    if len(idxs) > 0:
        for seq in box_seq:
            (x, y) = (boxes[seq][0], boxes[seq][1])  # 框左上角
            (w, h) = (boxes[seq][2], boxes[seq][3])  # 框宽高

            if classIDs[seq] == 0:  # 根据类别设定框的颜色

                color = [0, 0, 255]
            else:
                color = [0, 255, 0]

            cv2.rectangle(image, (x, y), (x + w, y + h), color, 2)  # 画出预测框
            position = [(x + w / 2) - (W / 2), (H / 2) - (y + h / 2)]
            positions.append(position)
            text = "{}: {:.4f}".format(LABELS[classIDs[seq]], confidences[seq], position)
            cv2.putText(image, text, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX, 0.3, color,
                        1)  # 输出预测框信息对应的数字和置信度

            '''
            print("位置信息：" + format(position))
            print("矩形框信息："+format(boxes))
            print("置信度信息："+format(confidences))
            print("类别标签信息："+format(classIDs))
            '''
            category = text
            xshaft = x
            yshaft = y

            print ("category第一个字符："+category[0])

            boxNum = category[0]

            intboxNum = int(boxNum)

            # 主要的三个值
            print("数字对应的矩形框：" + format(text) + " x坐标：" + format(x) + " y坐标：" + format(y))
            list.append(intboxNum)
            list.append(xshaft)
            list.append(yshaft)
            print("list数据:" + format(list))

    #cv2.namedWindow('Image', cv2.WINDOW_NORMAL)
    #cv2.imshow("Image", image)
    #cv2.waitKey(0)

    return list

print ("结束")

