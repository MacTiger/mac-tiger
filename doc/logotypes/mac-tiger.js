a=document.createElement("canvas")
a.style.backgroundColor="#eee"
a.width=a.height=256
b=a.getContext("2d")
b.lineWidth="8"
b.lineCap="round"
b.lineJoin="round"
b.strokeStyle="#000"
/* bas */
b.fillStyle="#f93"
b.beginPath()
b.ellipse(128,176,96,64,0,Math.PI,0,false)
b.ellipse(128,176,96,48,0,0,Math.PI,false)
b.closePath()
b.fill()
b.stroke()
/* viande */
b.fillStyle="#630"
b.beginPath()
b.ellipse(128,144,96,64,0,Math.PI,0,false)
b.ellipse(128,144,96,48,0,0,Math.PI,false)
b.closePath()
b.fill()
b.stroke()
/* fromage */
b.fillStyle="#fc0"
b.beginPath()
b.moveTo(16,120)
b.lineTo(128,56)
b.lineTo(240,120)
b.lineTo(128,184)
b.closePath()
b.fill()
b.stroke()
/* haut */
b.fillStyle="#f93"
b.beginPath()
b.ellipse(128,96,96,64,0,Math.PI,0,false)
b.ellipse(128,96,96,48,0,0,Math.PI,false)
b.closePath()
b.fill()
b.stroke()
/* babine gauche */
b.fillStyle="#fff"
b.beginPath()
b.ellipse(96,152,32,32,Math.PI/6,Math.PI,0,true)
b.fill()
b.stroke()
/* babine droite */
b.fillStyle="#fff"
b.beginPath()
b.ellipse(160,152,32,32,-Math.PI/6,0,Math.PI,false)
b.fill()
b.stroke()
/* moustache gauche */
b.beginPath()
b.moveTo(80,152)
b.lineTo(56,160)
b.moveTo(96,168)
b.lineTo(72,192)
b.stroke()
/* moustache droite */
b.beginPath()
b.moveTo(176,152)
b.lineTo(200,160)
b.moveTo(160,168)
b.lineTo(184,192)
b.stroke()
/* museau */
b.fillStyle="#f69"
b.beginPath()
b.moveTo(104,152)
b.lineTo(128,168)
b.lineTo(152,152)
b.fill()
b.stroke()
/* oeil gauche */
b.fillStyle="#fff"
b.beginPath()
b.ellipse(96,112,16,24,-Math.PI/12,Math.PI*2,0,false)
b.fill()
b.stroke()
/* oeil droit */
b.fillStyle="#fff"
b.beginPath()
b.ellipse(160,112,16,24,Math.PI/12,0,Math.PI*2,true)
b.fill()
b.stroke()
/* iris gauche */
b.beginPath()
b.arc(100,124,4,Math.PI*2,0,false)
b.closePath()
b.stroke()
/* iris droit */
b.beginPath()
b.arc(156,124,4,0,Math.PI*2,true)
b.closePath()
b.stroke()
/* oreille gauche */
b.fillStyle="#f69"
b.beginPath()
b.ellipse(80,80,32,48,-Math.PI/6,Math.PI,0,false)
b.fill()
b.stroke()
/* oreille droite */
b.fillStyle="#f69"
b.beginPath()
b.ellipse(176,80,32,48,Math.PI/6,0,Math.PI,true)
b.fill()
b.stroke()
/* raillures du bas */
b.fillStyle="#333"
b.beginPath()
b.moveTo(72,212)
b.lineTo(88,204)
b.lineTo(96,220)
b.moveTo(184,212)
b.lineTo(168,204)
b.lineTo(160,220)
b.fill()
b.stroke()
/* raillures du haut */
b.fillStyle="#333"
b.beginPath()
b.moveTo(112,36)
b.lineTo(128,60)
b.lineTo(144,36)
b.moveTo(104,80)
b.lineTo(128,72)
b.lineTo(152,80)
b.lineTo(128,88)
b.closePath()
b.fill()
b.stroke()
document.body.append(a)
