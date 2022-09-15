
dataList:{


//基本信息
// cs html.text()
   carSpeed:'',
   airPressure:'',
   breakPressure:'',

//空调基本信息



airPatternList: [[{         //模式 ,对应于mode字段
        airName: '1',
        airPattern: '制冷'
      },
      {
        airName: '1',
        airPattern: '制热'
      },
      {
        airName: '1',
        airPattern: '制冷'
      },
      {
        airName: '1',
        airPattern: '制冷'
      }, {
        airName: '1',
        airPattern: '制冷'
      }, {
        airName: '1',
        airPattern: '制冷'
      }], [
        {
          airName: '2',
          airPattern: '制冷'
        },
        {
          airName: '2',
          airPattern: '制热'
        },
        {
          airName: '2',
          airPattern: '制热'
        }, {
          airName: '2',
          airPattern: '制热'
        }, {
          airName: '2',
          airPattern: '制热'
        },
        {
          airName: '2',
          airPattern: '制热'
        }
      ]],
      airEditionList: [[{     //版本 ？？？
        airName: '1',
        airPattern: 'V1.0'
      },
      {
        airName: '1',
        airPattern: 'V1.0'
      },
      {
        airName: '1',
        airPattern: 'V1.0'
      },
      {
        airName: '1',
        airPattern: 'V1.0'
      }, {
        airName: '1',
        airPattern: 'V1.0'
      }, {
        airName: '1',
        airPattern: 'V1.0'
      }], [
        {
          airName: '2',
          airPattern: 'V2.0'
        },
        {
          airName: '2',
          airPattern: 'V2.0'
        },
        {
          airName: '2',
          airPattern: 'V2.0'
        }, {
          airName: '2',
          airPattern: 'V2.0'
        }, {
          airName: '2',
          airPattern: 'V2.0'
        },
        {
          airName: '2',
          airPattern: 'V2.0'
        }
      ]],

// 报警数TOP10

rankList: [{      //目前的报警好像无法针对到具体仪器
        "value": 32454,
        "name": "TC1-1机组加热器1"
      },
      {
        "value": 23240,
        "name": "TC1-1机组加热器2"
      },
      {
        "value": 22332,
        "name": "系列三"
      },
      {
        "value": 20925,
        "name": "系列四"
      },
      {
        "value": 19870,
        "name": "系列五"
      },
      {
        "value": 18756,
        "name": "系列6"
      },
      {
        "value": 17564,
        "name": "系列7"
      },
      {
        "value": 15678,
        "name": "系列8"
      },
      {
        "value": 14564,
        "name": "系列7"
      },
      {
        "value": 13678,
        "name": "系列8"
      }
      ],

//工单分布情况

warningList:[
              { value: 300, name: '待处理' },
              { value: 351, name: '已处理' },
              { value: 300, name: '挂起' },
              { value: 200, name: '已完成' }
            ],

//各空调报警分布情况
xDataList:['TC1', 'MP1', 'M1', 'M2', 'MP2', 'TC2'],
valueList:[33, 60, 25, 18, 12, 9]，

//实时故障

  realtimeFault: [
        {
          time: '03/23 10:17:15',   //时间
          level: '严重',          //级别
          sheb: 'G001',           //设备
          name: 'DCU通信故障',    //报警名称
          type: '故障'            //类型
        },
        {
          time: '03/23 10:17:15',
          level: '中等',
          sheb: 'G002',
          name: '门电流异常',
          type: '故障'
        }, {
          time: '03/23 10:17:15',
          level: '轻微',
          sheb: 'G003',
          name: '门电流异常',
          type: '故障'
        }, {
          time: '03/23 10:17:15',
          level: '轻微',
          sheb: 'G004',
          name: '门电流异常',
          type: '故障'
        }, {
          time: '03/23 10:17:15',
          level: '轻微',
          sheb: 'G005',
          name: '门电流异常',
          type: '故障'
        }, {
          time: '03/23 10:17:15',
          level: '轻微',
          sheb: 'G006',
          name: '门电流异常',
          type: '故障'
        },
        {
          time: '03/23 10:17:15',
          level: '轻微',
          sheb: 'G007',
          name: '门电流异常',
          type: '故障'
        }
      ],
//中间部分

//车厢展示     需要传递参数，判断是哪个车厢

      nameTC1: 'TC1',      //TC1车厢名称
      temperatureTC1: '25',  //TC1车厢温度
	  statusTCl:'正常',       //正常、故障、预警
      nameTC2: 'TC2',
      temperatureTC2: '26',
	  statusTC2:'正常',       //正常、故障、预警
      carriageList: [
        {
          name: 'MP1',
          temperature: '23',
		  statusType:'正常',       //正常、故障、预警
        }, {
          name: 'M1',
          temperature: '26',
		  statusType:'正常',
        },
        {
          name: 'M2',
          temperature: '23',
		  statusType:'正常',
        }, {
          name: 'MP2',
          temperature: '23',
		  statusType:'正常',
        }],
// 空调  --->默认是TC1车厢的数据，TC2,MP1,MP2,M1,M2车厢所需数据结构与TC1一致

// 空调一默认展示系统一，风凝回路    --->系统2的与系统1数据结构一致
 tc1Hvac1S1IDamperTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],    //新风温度
 tc1Hvac1S1ReturnDamperTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],      //回风温度
 tc1Hvac1S1SendDamperTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],      //送风温度
 tc1Hvac1ITargetTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],     //目标温度
 xDateOneList:['1','2',.....],                                              //横坐标，24小时

//冷凝回路

tc1Hvac1S1CoolTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],     //冷凝温度
tc1Hvac1S1InhaleTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],    //吸气温度
tc1Hvac1S1ExhaustTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],   //排气温度
tc1Hvac1S1OutEvaporationTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......], //蒸出温度
tc1Hvac1S1EvaporationTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],    //蒸发温度

 tc1Hvac1ReturnDamperState：'',  //回风阀:两个状态   开：open  关：closed
 tc1Hvac1IDamperState:'',      //新风阀:两个状态   开：open  关：closed
 tc1Hvac1Mode:'',                 //空调模式
 tc1Hvac1IExtTemp:'',          //室外温度
 tc1Hvac1ITargetTemp:'',       //目标温度

 tc1Hvac1Compressor1State:'', //压缩机1状态
 tc1Hvac1Compressor2State:'',  //压缩机2状态
 tc1Hvac1Ventilation1State:'',  //通风机1状态
 tc1Hvac1Ventilation2State:'',  //通风机2状态
 tc1Hvac1Heater1State:'',       //电加热器1状态
 tc1Hvac1Heater2State:'',       //电加热器2状态


 // 空调二默认展示系统一，风凝回路       --->系统2的与系统1数据结构一致
 tc1Hvac1S1IDamperTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],    //新风温度
 tc1Hvac1S1ReturnDamperTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],      //回风温度
 tc1Hvac1S1SendDamperTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],      //送风温度
 tc1Hvac1ITargetTemp：[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],     //目标温度
 xDateOneList:['1','2',.....],

 //冷凝回路
tc1Hvac2S1CoolTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],     //冷凝温度
tc1Hvac2S1InhaleTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],    //吸气温度
tc1Hvac2S1ExhaustTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],   //排气温度
tc1Hvac2S1OutEvaporationTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......], //蒸出温度
tc1Hvac2S1EvaporationTemp:[25, 23, 33, 32, 23, 22, 22, 22, 22, 23, 23, 24,......],    //蒸发温度


 tc1Hvac2ReturnDamperState：'',  //回风阀:两个状态   开：open  关：closed
 tc1Hvac2IDamperState:'',      //新风阀:两个状态   开：open  关：closed
 tc1Hvac2Mode:'',                 //空调模式
 tc1Hvac2IExtTemp:'',          //室外温度
 tc1Hvac2ITargetTemp:'',       //目标温度

 tc1Hvac2Compressor1State:'', //压缩机1状态
 tc1Hvac2Compressor2State:'',  //压缩机2状态
 tc1Hvac2Ventilation1State:'',  //通风机1状态
 tc1Hvac2Ventilation2State:'',  //通风机2状态
 tc1Hvac2Heater1State:'',       //电加热器1状态
 tc1Hvac2Heater2State:'',       //电加热器2状态


}