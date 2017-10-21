
%compound box of nsgaii and moea/d into one plot

%draw solutions of nsgaii
variable=FUN_NSGAII;
x=variable(:,1);
y=variable(:,2);
z=variable(:,3);
c=variable(:,4);

M=[log(x),log(y),log(z)];
drawGreyBoxes(M);
hold on;
scatter3(gca,log(x),log(y),log(z),10,log(c),'filled');
hold on;



%draw solutions of moea/d
variable=FUN_MOEAD_d;
x=variable(:,1);
y=variable(:,2);
z=variable(:,3);
c=variable(:,4);

M=[log(x),log(y),log(z)];
drawRedBoxes(M);
scatter3(gca,log(x),log(y),log(z),10,log(c),'filled');



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


%view
camproj(gca,'perspective');
view(gca,[-42,20]);

%x-y view
%view([0,-90]);
%y-z view
%view([90,0]);
%x-z view
%view([0,0]);
