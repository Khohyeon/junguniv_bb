// 접속 브라우저 비중
	const doughChart_01 = document.getElementById('browser_chart').getContext('2d');
	const browser_chart = new Chart(doughChart_01, {
		plugins: [ChartDataLabels],
		type: 'doughnut',
		data: {
			labels: ['크롬', '엣지', '웨일', '사파리', '기타'],
			datasets: [{
				label: '접속브라우저',
				data: [2516, 505, 153, 382, 186], //값
				backgroundColor: [
					'rgba(254, 90, 123, 0.9)',
					'rgba(253, 172, 65, 0.9)',
					'rgba(91, 141, 238, 0.9)',
					'rgba(75, 192, 192, 0.9)',
					'rgba(100, 100, 100, 0.9)',
				],
				borderWidth: 0,
			}]
		},
		 options: {
			responsive:false,
			plugins:{
				datalabels: {
					clamp:true,
					color:'#fff',
					font:{
						size:12,
						family:'Pretendard-Medium',
					},
					formatter: function (value, context) {
						return Math.round(value / context.chart.getDatasetMeta(0).total * 100) + "%";
					}
				},
				legend:{
					display:true,
					position:'top',
					labels:{
						boxWidth:12,
						boxHeight:8,
						font:{
							family:'Pretendard-Medium',
						}
					},
				},	
			},
		},
	});

	// 시간대별 접속 현황 (line chart)
	const lineChart_02 = document.getElementById('time_chart').getContext('2d');
	const time_chart = new Chart(lineChart_02, {
		plugins: [ChartDataLabels],
		type: 'line',
		data: {
			labels: ['1시','2시','3시','4시','5시','6시','7시','8시','9시','10시','11시','12시','13시','14시','15시','16시','17시','18시','19시','20시','21시','22시','23시','24시'],
			datasets: [{
				label: '인원수',
				data: [123,51,100,152,23,123,51,22,2,23,98,51,55,13,23,5,51,55,44,23,123,51,18,5],  //값
				backgroundColor: 'rgba(169, 123, 255, 0.9)',
				borderColor:'rgba(169, 123, 255, 0.9)',
				borderWidth:1,
				pointStyle: 'circle',
				pointRadius: 4,
				pointHoverRadius: 8,
			}]
		},
		options: {
			scales: {
				y: {
					beginAtZero: true,
					ticks:{
						stepSize:20
					}
				},
			},
			plugins:{	
				legend:{ // label 숨김 
					display:false
				},
				datalabels: {
					align:'end',
					anchor:'end',
					offset:-3,
					clamp:true,
					color:'black',
					font:{
						family:'Pretendard-Bold',
					}
				},
			},
		}
	});


	// 월별 수강등록 인원 차트 (bar chart)
	const barChart_02 = document.getElementById('new_member_chart').getContext('2d');
	const new_member_chart = new Chart(barChart_02, {
		plugins: [ChartDataLabels],
		type: 'bar',
		data: {
			labels: ['5월', '6월', '7월', '8월', '9월'],
			datasets: [{
				label: '인원수',
				data: [3000, 2500, 1530, 3820, 2010],  //값
				backgroundColor: [
					'rgba(254, 90, 123, 0.9)',
					'rgba(253, 172, 65, 0.9)',
					'rgba(91, 141, 238, 0.9)',
					'rgba(75, 192, 192, 0.9)',
					'rgba(94, 188, 89, 0.9)',
				],
				borderWidth: 0,
				borderRadius:3,
				barThickness: 50,
			}]
		},
		options: {
			scales: {
				y: {
					beginAtZero: true,
					ticks:{
						stepSize:1000
					}
				}

			},
			plugins:{	
				legend:{ // label 숨김 
					display:false
				},
				datalabels: {
					align:'end',
					anchor:'end',
					offset:-3,
					clamp:true,
					color:'black',
					font:{
						family:'Pretendard-Bold',
					}
				},
			},
		}
	});

	//월별 접속 기기 비중
	const barChart_03 = document.getElementById('device_chart').getContext('2d');
	const device_chart = new Chart(barChart_03, {
		plugins: [ChartDataLabels],
		data: {
			labels: ['5월', '6월', '7월', '8월', '9월'],
			datasets: [{
				type: 'bar',
				label: 'PC',
				backgroundColor:'rgba(169, 123, 255, 0.9)',
				borderWidth: 0,
				borderRadius:3,
				data: [3000, 2500, 1530, 3820, 2010]  //PC 값
			},{
				type: 'bar',
				label: 'MOBILE',
				backgroundColor:'rgba(169, 123, 255, 0.1)',
				borderColor:'rgba(169, 123, 255, 0.9)',
				borderWidth: 1,
				borderRadius:3,
				data: [2000, 881, 1518, 1511, 5530]  //Mobile 값
				}]
		},
		options: {
			scales: {
				y: {
					beginAtZero: true,
					ticks:{
						stepSize:1000
					}
				}

			},
			plugins:{	
				datalabels: {
					align:'end',
					anchor:'end',
					offset:-3,
					clamp:true,
					color:'black',
					font:{
						family:'Pretendard-Bold',
					}
				},
			},
		}
	});