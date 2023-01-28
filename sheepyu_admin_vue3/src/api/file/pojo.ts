export interface SystemFileRespVo {
  id: number;

  uploadId: string

  //("文件名")
  filename: string;

  //("md5")
  md5: string;

  //("url")
  url: string;

  //("mimeType")
  mimeType: string;

  //("size")
  size: number;

  //("地域")
  domain: string;

  //("相对路径")
  path: string;

  //("是否完成")
  complete: boolean;

  //("备注")
  remark: string;

  //("如果没有完成, 已完成的最后一块分片的坐标")
  partIndex: number;
}
