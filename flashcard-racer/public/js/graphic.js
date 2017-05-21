function graphicPoints(maxPoints,javaPoints,incorrectValue,pieContainer){
	/*
	 * To get it to work the following valus are needed maxPoints(int),
	 * maxTime(int), javaTime(int), javaPoints(int) /*Gets the "points" from the
	 * javaprogram
	 */
           var points = javaPoints;
		   
           color = d3.scale.category20c();
			/* sets the size of the points scale w = widht and h = hight */
            var w = 360,
                h = 180;

			/* sets the inner and outer arcs of the bar */	
            var radius = {
                "inner": h / 1.9,
                "outer": w / 3.3
            }

            var pi = Math.PI;

          
            var svgElem = pieContainer
                .append('svg')
                .attr('width', w)
                .attr('height', h)
			/* To get the half of a circle */
            var plotGroup = svgElem.append('g')
                .attr("transform", "translate(" + w / 2 + "," + h + ")")



            var color = ['#B2B2B2', '#f6cc56', '#888888'];

         
            /* Defining Radius */
            var arc = d3.svg.arc()
                .innerRadius(radius.inner)
                .outerRadius(radius.outer)
                .startAngle(0)
                .endAngle(Math.PI);


            var path = plotGroup.append('path')
                .attr({
                    d: arc,
                    transform: 'rotate(-90)'
                }).attr({
                    'stroke-width': "1",
                    stroke: "#666666"
                })
                .style({
                    fill: color[0]
                });

            var arcLine = d3.svg.arc()
                .innerRadius(radius.inner)
                .outerRadius(radius.outer)
                .startAngle(0)



            var pathForeground = plotGroup.append('path')
                .datum({
                    endAngle: 0
                })
                .attr({
                    d: arcLine,
                    transform: 'rotate(-90)'
                })
                .style({
                    fill: function(d, i) {
                     
                      
                      if(points>39 && points<=84){
                         return color[1];
                      }
                      if(points<=39){
                        return "#E54D41";
                      }
                      if(points>84){
                        return "#CFD45C"
                      }
                       
                    }
                });


            var oldValue = 0;

            var arcTween = function(transition, newValue, oldValue) {
                transition.attrTween("d", function(d) {
                 
                    var interpolate = d3.interpolate(d.endAngle, ((Math.PI)) * (newValue / maxPoints));

                    var interpolateCount = d3.interpolate(oldValue, newValue);

                    return function(t) {
                        d.endAngle = interpolate(t);
                        middleCount.text(Math.floor(interpolateCount(t)) + '/'+maxPoints+' correct ');

                        return arcLine(d);
                    };
                });
            };




            var middleCount = plotGroup.append('text')
                .datum(0)
                .text(function(d) {
                    return d;
                })
                .attr({
                    class: 'middleText',
                    'text-anchor': 'middle',
                    dy: -12,
                    dx: 3
                })
                .style({
                    fill: d3.rgb('#000000'),
                    'font-size': '16px'
                })

            plotGroup.append('text').text("/n").text(incorrectValue+ '/'+maxPoints+' incorrect').attr({
                    class: 'middleText',
                    'text-anchor': 'middle',
                    dy: -2,
                    dx: 3
                })
                .style({
                    fill: d3.rgb('#FF0000'),
                    'font-size': '12px'
                })

            pathForeground.transition()
                .duration(750)
                .ease('cubic')
                .call(arcTween, points, oldValue)

}


function graphicTime(javaTime,maxTime,pieContainer,timeValue){
  
    
/* JAVACODE POINTER CURRENT T */
			var time = javaTime;
           
           color = d3.scale.category20c();

            var w = 360,
                h = 180;

            var radius = {
                "inner": h / 1.9,
                "outer": w / 3.3
            }

            var pi = Math.PI;
            if(javaTime<maxTime){
            	var svgId = 'svg'+timeValue;
            	d3.select("#"+svgId).remove();
            }
            var svgElem = pieContainer
            .append('svg').attr('id','svg'+javaTime)
            .attr('width', w)
            .attr('height', h);


            var plotGroup = svgElem.append('g')
                .attr("transform", "translate(" + w / 2 + "," + h + ")")



            var color = ['#B2B2B2', '#f6cc56', '#888888'];

         
            /* Defining Radius */
            var arc = d3.svg.arc()
                .innerRadius(radius.inner)
                .outerRadius(radius.outer)
                .startAngle(0)
                .endAngle(Math.PI);


            var path = plotGroup.append('path')
                .attr({
                    d: arc,
                    transform: 'rotate(-90)'
                }).attr({
                    'stroke-width': "1",
                    stroke: "#666666"
                })
                .style({
                    fill: color[0]
                });

            var arcLine = d3.svg.arc()
                .innerRadius(radius.inner)
                .outerRadius(radius.outer)
                .startAngle(0)



            var pathForeground = plotGroup.append('path')
                .datum({
                    endAngle: 0
                })
                .attr({
                    d: arcLine,
                    transform: 'rotate(-90)'
                })
                .style({
                    fill: function(d, i) {
                     
                      
                      if(time>9 && time<=15){
                         return color[1];
                      }
                      if(time<=9){
                        return "#E54D41";
                      }
                      if(time>15){
                        return "#CFD45C"
                      }
                       
                    }
                });


            var oldValue = 0;

            var arcTween = function(transition, newValue, oldValue) {
                transition.attrTween("d", function(d) {
                 
                    var interpolate = d3.interpolate(d.endAngle, ((Math.PI)) * (newValue / maxTime));

                    var interpolateCount = d3.interpolate(oldValue, newValue);

                    return function(t) {
                        d.endAngle = interpolate(t);
                        middleCount.text(Math.floor(interpolateCount(t)) + 'S');

                        return arcLine(d);
                    };
                });
            };




            var middleCount = plotGroup.append('text')
                .datum(0)
                .text(function(d) {
                    return d;
                })
                .attr({
                    class: 'middleText',
                    'text-anchor': 'middle',
                    dy: -12,
                    dx: 3
                })
                .style({
                    fill: d3.rgb('#000000'),
                    'font-size': '12px'
                })

            plotGroup.append('text').text('Time').attr({
                    class: 'middleText',
                    'text-anchor': 'middle',
                    dy: -2,
                    dx: 3
                })
                .style({
                    fill: d3.rgb('#000000'),
                    'font-size': '12px'
                })

            pathForeground.transition()
                .duration(750)
                .ease('cubic')
                .call(arcTween, time, oldValue)

}