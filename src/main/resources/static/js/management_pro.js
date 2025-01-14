//학습진행현황 (gauge chart)
const gaugeChart = document.getElementById('learning_progress_gauge').getContext('2d');
const value = 75; //값
const max = 100;  //최대값
const percent = value / max;  //백분율
var gradient_bg = gaugeChart.createLinearGradient(0, 0, 200, 0);   //gradient bg
gradient_bg.addColorStop(0, 'rgba(254,90,123,1)');
gradient_bg.addColorStop(0.5, 'rgba(253,172,65,1)');
gradient_bg.addColorStop(1, 'rgba(57,218,138,1)');

const learning_progress_gauge = new Chart(gaugeChart, {
	plugins: [ChartDataLabels],
   type: 'doughnut',
   data: {
		datasets: [{
		label: "학습진행현황",
		backgroundColor: [
			gradient_bg,
			'#ddd'
		],
		data: [ //데이터 값
			percent * 100,
			100 - (percent * 100)
		],
		borderWidth:0,
		cutout: "60%"
	  }]
	},

	options: {
		rotation: 270,
		circumference: 180,
		responsive:false,
		plugins:{
			datalabels: {
				clamp:true,
				color:'#fff',
				font:{
					size:16,
					family:'Pretendard-Medium',
				},
			formatter: function (value, context) {
					return Math.round(value / context.chart.getDatasetMeta(0).total * 100) + "%";
				}
			},
			tooltip:{
				enabled:false,
			},	
		},
	}
});


//평가응시현황 (progress bar)
$(function() {
	$('.progress > span.pg').each(function() {
		var max = $(this).data('progress');
		$(this).css('width', max + '%');
	});
});


//1년 수료 현황 (pie chart)
const pieChart_01 = document.getElementById('year_completion_chart').getContext('2d');
const year_completion_chart = new Chart(pieChart_01, {
	plugins: [ChartDataLabels],
   type: 'pie',
   data: {
	  labels: ["미수료", "수료"],
	  datasets: [{
		label: "인원수",
		backgroundColor: [
			'rgba(254, 90, 123, 1)',
			'rgba(253, 172, 65, 1)'
		],
		data: [554,850],  //값
		borderWidth:0,
	  }]
	},

	options: {
		responsive:false,
		plugins:{
			datalabels: {
				clamp:true,
				color:'#fff',
				font:{
					size:15,
					family:'Pretendard-Medium',
				},
				formatter: function (value, context) {
					return Math.round(value / context.chart.getDatasetMeta(0).total * 100) + "%";
				}
			},
			legend:{
				display:true,
				reverse:true,
				position:'right',
				labels:{
					boxWidth:20,
					boxHeight:8,
					font:{
						family:'Pretendard-Medium',
					}
				},
				onClick:false,
			},	
		},
	}
});


// 월별 수강등록 인원 차트 (bar chart)
const barChart_01 = document.getElementById('registered_chart').getContext('2d');
const registered_chart = new Chart(barChart_01, {
	plugins: [ChartDataLabels],
	type: 'bar',
	data: {
		labels: ['5월', '6월', '7월', '8월', '9월'], //x축
		datasets: [{
			label: '인원수',
			data: [3000, 2500, 1530, 3820, 2010], //값
			backgroundColor: [
				'rgba(254, 90, 123, 0.9)',
				'rgba(253, 172, 65, 0.9)',
				'rgba(91, 141, 238, 0.9)',
				'rgba(75, 192, 192, 0.9)',
				'rgba(94, 188, 89, 0.9)',
			],
			borderWidth: 0,
			borderRadius:3,
			barThickness: 48,
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

//월별 수료 인원 (line chart)
const lineChart_01 = document.getElementById('completion_chart').getContext('2d');
const completion_chart = new Chart(lineChart_01, {
	plugins: [ChartDataLabels],
	type: 'line',
	data: {
		labels: ['5월', '6월', '7월', '8월', '9월'], //x축
		datasets: [{
			label: '인원수',
			data: [1025, 5629, 2054, 3005, 1822, 3504], //값
			backgroundColor: [
				'rgba(254, 90, 123, 1)',
				'rgba(253, 172, 65, 1)',
				'rgba(91, 141, 238, 1)',
				'rgba(75, 192, 192, 1)',
				'rgba(94, 188, 89, 1)',
			],
			borderColor: [
				'rgba(254, 90, 123, 1)',
				'rgba(253, 172, 65, 1)',
				'rgba(91, 141, 238, 1)',
				'rgba(75, 192, 192, 1)',
				'rgba(94, 188, 89, 1)',
			],
			borderWidth:1,
			pointStyle: 'circle',
			pointRadius: 6,
			pointHoverRadius: 10,
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
				},
			},
		},
	}
});