# tic tac toe (Reagent ver)

done with "[Tutorial: Into To React](https://reactjs.org/tutorial/tutorial.html)" using Reagent (ClojureScript)

### FYI

* [Reagent](http://reagent-project.github.io/)
* [reagent.core](http://reagent-project.github.io/docs/master/reagent.core.html)
* [ClojureStcript Cheatsheet](http://cljs.info/cheatsheet/)

## Demo

https://f6o.github.io/tick/

### Development mode

To start the Figwheel compiler, navigate to the project folder and run the following command in the terminal:

```
lein figwheel
```

Figwheel will automatically push cljs changes to the browser.
Once Figwheel starts up, you should be able to open the `public/index.html` page in the browser.


### Building for production

```
lein clean
lein package
```
