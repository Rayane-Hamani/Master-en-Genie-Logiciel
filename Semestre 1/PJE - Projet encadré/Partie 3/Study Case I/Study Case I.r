# Projet 1 : Analysis and Forecasting of House Price Indices





##### Importing HPI Data #####

# Pour commencer, nous allons importer le fichier 'House-index-canberra.csv'

hpi <- read.csv(file='House-index-canberra.csv', header=FALSE)

# Nous allons ensuite donner un nom aux colonnes du dataframe

names(hpi) <- c('date', 'indice') # remplace V1 et V2

# Et enfin nous allons convertir les factors en type date

Sys.setlocale('LC_TIME', 'C')

hpi$date <- as.Date(x=hpi$date, format='%d-%b-%y')
# as.numeric(format(hpi$date, '%y')) pour récupérer une partie des dates





##### Exploration of HPI Data #####

# A partir d'ici, nous allons commencer à jouer avec les indices
# afin d'obtenir les plages de données que l'on souhaite pour nos graphes

annees <- unique(as.numeric(format(hpi$date, '%Y')))
mois   <- unique(as.numeric(format(hpi$date, '%m')))

nbAnnees <- length(annees)
nbMois   <- length(mois)

tousLes1ans <- seq(1, length(hpi$date), 12)
tousLes3ans <- seq(1, length(hpi$date), 36)
tousLes5ans <- seq(1, length(hpi$date), 60)

nbLignes <- nrow(hpi)

varMensuelle <- hpi$indice - c(1, hpi$indice[-253])
varAnnuelleBrut  <- hpi$indice[tousLes1ans[-1]] - hpi$indice[tousLes1ans[-length(tousLes1ans)]]
varAnnuellePour  <- hpi$indice[tousLes1ans[-1]] / hpi$indice[tousLes1ans[-length(tousLes1ans)]] -1

# Evolution des indices entre 1990 et 2011

# La courbe des indices évolue de façon stable jusqu'en l'an 2000
# Depuis cette date, elle croît de façon exponentielle sans donner des signes
# d'arrêts
# Si la courbe avait continué son évolution de 2000 à 2010 comme elle avait fait
# de 1990 à 2000, son indice en 2010 aurait été de 2,5 au maximum
# Il est pourtant presque 2 fois supérieur en 2010

plot(main='Evolution des indices',
     x=hpi$date, y=hpi$indice, type='l',
     xlab='dates', ylab='indices', col='blue')

grid()

plot(main='Evolution des indices',
     x=hpi$date, y=hpi$indice, type='p',
     xlab='dates', ylab='indices',
     col=ifelse(varMensuelle>0, 'green', 'red'),
     pch=ifelse(varMensuelle>0, 'x', 'x'))

grid()

# Variation mensuelle brute des indices pendant cette période

# On peut voir peu après 1995 qu'une croissance intervient et atteint son pic
# peu avant 2005
# Depuis, les indices sont très hétérogènes montrant soit une forme
# d'instabilité du marché, soit une évolution qui perdure et qui n'est pas prêt
# de s'arrêter vu la grande disparité

plot(main='Variation mensuelle brute',
     x=hpi$date, y=varMensuelle, type='p',
     xlab='dates', ylab='variation entre chaque mois',
     col=ifelse(varMensuelle>0, 'green', 'red'), pch='°')

grid()

# Variation annuelle brute des indices pendant cette période

# On peut voir ici un aperçu de la croissance des indices chaque année
# Même lorsque les indices décroissent, c'est toujours par un faible taux
# Ce qui est d'autant plus intéressant, c'est que les indices croissent avec un
# fort taux par rapport à l'année précédente 1 fois sur 2 depuis l'an 2000

plot(main='Variation annuelle brute',
     x=annees[-length(annees)], y=varAnnuelleBrut, type='b',
     xlab='dates', ylab='variation entre chaque année',
     col=ifelse(varAnnuelleBrut>0, 'green', 'red'), pch='o')

grid()

# Tendance d'évolution annuelle des indices

# Cet histogramme donne un aperçu de la tendance des indices par rapport aux
# années précédentes
# Sur les dernières années, c'est une tendance à +0,3 qui semble s'être opérée

barplot(main='Variations annuelles brute',
        varAnnuelleBrut,
        col=ifelse(varAnnuelleBrut>0, 'green', 'red'))

grid()

# En terme de pourcentage, cela donne aussi des chiffres à la hausse

barplot(main='Variations annuelles en pourcentage',
        varAnnuellePour*100,
        col=ifelse(varAnnuellePour>0, 'green', 'red'))

grid()





##### Trend and Seasonal Components of HPI #####



hpi <- ts(houseIndex$index, start=c(1990,1),frequency = 12)
f <- stl(hpi,"per")
plot(f)

# Seasonal components
plot(f$time.series[1:12,"seasonal"],type='b',xlab="Month",ylab="Seasonal Components")

# An alternative decomposition function
f2 <- decompose(hpi)
plot(f2)
plot(f2$figure,type="b",xlab="Month",ylab="Seasonal Components")

# HPI Forecasting
startYear <- 1990
endYear <- 2010
# forecast HPIs in the next four years
nYearAhead <- 4
"
  order (p,d,q)
  p = order of the autoregressive part;
  d = degree of first differencing involved;
  q = order of the moving average part.
  "
fit <- arima(hpi,order=c(2,0,1), seasonal=list(order=c(2,1,0),period=12))
fore <- predict(fit, n.ahead=12*nYearAhead)
U <- fore$pred + 2 * fore$se
L <- fore$pred - 2 * fore$se
ts.plot(hpi,fore$pred, U, L, col=c("black", "blue","green","red"), lty=c(1,5,2,2), gpars=list(xaxt="n", xlab=""), ylab="Index", main="House Price Trading Index Forecast (Canberra)")
years <- startYear:(endYear + nYearAhead+1)
axis(1, labels=paste("Jan ", years, sep=""), las=3, at=years)

grid()

legend("topleft", col=c("black", "blue", "green", "red"), lty=c(1,5,2,2),  c("Actual Index", "Forecast", "Upper Bound (95% Confidence)", "Lower Bound (95 % Confidence)"))

ts.plot(fore$pred, U, L, col=c("blue","green","red"), lty=c(5,2,2), gpars=list(xaxt="n", xlab=""), ylab="Index", main="House Price Trading Index Forecast (Canberra)")

years <- endYear + (1:(nYearAhead+1))

axis(1, labels=paste("Jan ", years, sep=""), las=3, at=years)

grid(col="gray",lty="dotted")

legend("topleft", col=c("blue", "green", "red"), lty=c(5,2,2),  c("Forecast", "Upper Bound (95% Confidence)", "Lower Bound (95 % Confidence)"))

