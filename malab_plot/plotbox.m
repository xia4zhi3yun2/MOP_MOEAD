
%plot box of moea/d and nsgaii



%variable=FUN_NSGAII;
variable=FUN_MOEAD_d;
x=variable(:,1);
y=variable(:,2);
z=variable(:,3);
c=variable(:,4);



M=[log(x),log(y),log(z)];
drawGreyBoxes(M);
scatter3(gca,log(x),log(y),log(z),10,log(c),'filled');


camproj(gca,'perspective');
view(gca,[-42,20]);

%axial label 
xlabel(gca,'logFNR');
ylabel(gca,'logFPR');
zlabel(gca,'logUR');
set(gca,'ZDir','reverse')

%draw colorbar
caxis([-6,0]);
colorbar('EastOutside');
h = colorbar;
ylabel(h, 'logCR','fontsize',16)
hold on;




