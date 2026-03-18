TUTORIAL DE NEXT STEPS:


*Ver antes de empezar con otra entidad!*

**Mapper -> https://chatgpt.com/share/69ba5757-821c-8002-ae4b-193785aad581**

    Ten en cuenta que los ejemeplos pueden ser algo erróneos o tener algun fallito porque no los he comprobado. Pero 
    ahí tienes toda la teoría, como aplicarlo, por qué y demás. Seguramente declares la clase y no funcione simplemente
    teniendo la interfaz y es, porque como indica, son métodos que se generan en ejecución, que quiere decir esto? Que
    tienes que lavanzar el maven clean install a la derecha y con ello, generará las clases en la siguiente ruta:

    target > generated-sources

    También puedes acceder a ello desde el método de la interfaz, te saldrá un simbolito a la izquierda verde de ir a 
    la implementación y puedes cotillear lo que hace la librería por detrás, sin embargo, con poner bien el nombre y,
    en caso necesario, algunas anotaciones, ya te lo hace solo.

    Por último, al final de la conversación, tienes un pequeño SPIKE de lo que se puede hacer con el mapStruct por si te 
    lo encuentras por ahí.

**Pagable -> https://chatgpt.com/share/69ba5994-62fc-8002-a76f-c02e84d14521**

    Un poco de la misma forma, es un tutorial de como implementar la paginación. Es algo bastante sencillo y le he
    hecho una pregunta de porque con solo eso es sencillo para que te explique. Si te entra alguna duda o problema, 
    entre ChatGPT y preguntarme a mí debería quedar claro, pero vaya que no tiene nada de misterio jeje.

**Validations -> https://chatgpt.com/share/69ba5a62-1a3c-8002-86e0-008ebfcd5348**

    Esta parte es también sencilla, es para que veas con las anotaciones como se peude validar todo. Importante, esto es
    solo a nivel de Controller y los objetos Request. Solo funciona si le añades las validaciones en crudo en el 
    @RequestParam o delante del @RequestBody le metes un @Validate.

    De la primera respuesta que me da te puedes omitir los puntos 5 y 8 que ya es un nivel muy avanzado que nunca he visto
    usar en un poryecto real. Ahora, si te suena guay, pero no hace falta que las implementes.


**PDTE -> Security, Testing, DocSwagger, JavaDoc**